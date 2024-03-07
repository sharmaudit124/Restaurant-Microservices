package com.restaurant.menuservice.services;

import com.restaurant.menuservice.dtos.*;
import com.restaurant.menuservice.entities.Category;
import com.restaurant.menuservice.entities.Item;
import com.restaurant.menuservice.enums.ItemType;
import com.restaurant.menuservice.exceptions.CustomIllegalException;
import com.restaurant.menuservice.exceptions.ElementAlreadyExistException;
import com.restaurant.menuservice.exceptions.NoElementFoundException;
import com.restaurant.menuservice.repositories.ItemRepository;
import com.restaurant.menuservice.utils.StringJsonConverter;
import com.restaurant.menuservice.utils.UUIDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@CacheConfig(cacheNames = {"items"})
public class ItemServiceImpl implements ItemCrudService, ItemConditionService {
    public static final ItemResponseDTO NULL_ITEM = new ItemResponseDTO();
    private final CacheManager cacheManager;

    private final ItemRepository itemRepository;

    private final CategoryServiceImpl categoryService;

    private final StringJsonConverter stringJsonConverter;

    private final UUIDGenerator uuidGenerator;

    private final ModelMapper modelMapper;

    @Autowired
    public ItemServiceImpl(CacheManager cacheManager, ItemRepository itemRepository, CategoryServiceImpl categoryService, StringJsonConverter stringJsonConverter, UUIDGenerator uuidGenerator, ModelMapper modelMapper) {
        this.cacheManager = cacheManager;
        this.itemRepository = itemRepository;
        this.categoryService = categoryService;
        this.stringJsonConverter = stringJsonConverter;
        this.uuidGenerator = uuidGenerator;
        this.modelMapper = modelMapper;
    }

    @Override
    public ItemResponseDTO createItem(ItemRequestDTO itemRequestDTO) {
        Optional<Item> existingItem = itemRepository.findByItemNameIgnoreCase(itemRequestDTO.getItemName());
        if (existingItem.isPresent()) {
            throw new ElementAlreadyExistException("item already present with given name : " + itemRequestDTO.getItemName(), NULL_ITEM);
        }

        ItemType type = itemRequestDTO.getItemTypeEnum();

        CategoryDTO category = categoryService.findCategoryByCategoryName(itemRequestDTO.getCategory().getCategoryName());


        Discount discount = itemRequestDTO.getPrice().getDiscount();
        Double actualPrice = itemRequestDTO.getPrice().getItemPrice();
        Double discountPercentage = itemRequestDTO.getPrice().getDiscount().getDiscountPercentage();
        discount.setDiscountedPrice(actualPrice - (actualPrice * discountPercentage / 100));

        String discountDetails = stringJsonConverter.getStringifyDiscountDetails(discount);

        Item item = new Item();
        item.setItemId(uuidGenerator.generateUUIDString());
        item.setItemName(itemRequestDTO.getItemName());
        item.setItemDetails(itemRequestDTO.getItemDetails());
        item.setItemType(type);
        item.setDefaultPrice(itemRequestDTO.getPrice().getItemPrice());
        item.setDiscountDetails(discountDetails);
        item.setImageUrl(itemRequestDTO.getImageUrl());
        item.setCategory(modelMapper.map(category, Category.class));
        log.info("Saving item in MySQL");
        itemRepository.save(item);

        Discount discount1 = stringJsonConverter.getDiscountDTO(item.getDiscountDetails());
        PriceDTO priceDTO = new PriceDTO(item.getDefaultPrice(), discount1);

        ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
        itemResponseDTO.setItemId(item.getItemId());
        itemResponseDTO.setItemName(item.getItemName());
        itemResponseDTO.setItemDetails(item.getItemDetails());
        itemResponseDTO.setItemType(item.getItemType().name());
        itemResponseDTO.setCategory(category);
        itemResponseDTO.setImageUrl(item.getImageUrl());
        itemResponseDTO.setPrice(priceDTO);

        return itemResponseDTO;
    }

    @Override
    public ItemResponseDTO updateItem(ItemResponseDTO itemResponseDTO) {
        Optional<Item> optionalItem = itemRepository.findById(itemResponseDTO.getItemId());

        if (optionalItem.isEmpty()) {
            throw new NoElementFoundException("Item with given id is not present.", NULL_ITEM);
        }
        ItemType type = itemResponseDTO.getItemTypeEnum();

        CategoryDTO category = categoryService.findCategoryByCategoryName(itemResponseDTO.getCategory().getCategoryName());

        Discount discount = itemResponseDTO.getPrice().getDiscount();
        Double actualPrice = itemResponseDTO.getPrice().getItemPrice();
        Double discountPercentage = itemResponseDTO.getPrice().getDiscount().getDiscountPercentage();
        discount.setDiscountedPrice(actualPrice - (actualPrice * discountPercentage / 100));

        String discountDetails = stringJsonConverter.getStringifyDiscountDetails(discount);

        Item existingItem = optionalItem.get();
        existingItem.setItemId(itemResponseDTO.getItemId());
        existingItem.setItemName(itemResponseDTO.getItemName());
        existingItem.setItemDetails(itemResponseDTO.getItemDetails());
        existingItem.setItemType(type);
        existingItem.setCategory(modelMapper.map(category, Category.class));
        existingItem.setImageUrl(itemResponseDTO.getImageUrl());
        existingItem.setDefaultPrice(itemResponseDTO.getPrice().getItemPrice());
        existingItem.setDiscountDetails(discountDetails);

        log.info("updating item in MySQL");
        itemRepository.save(existingItem);
        evictCacheByName(itemResponseDTO.getItemId());

        Discount discount1 = stringJsonConverter.getDiscountDTO(existingItem.getDiscountDetails());
        PriceDTO priceDTO = new PriceDTO(existingItem.getDefaultPrice(), discount1);

        ItemResponseDTO itemResponseDTO1 = new ItemResponseDTO();
        itemResponseDTO.setItemId(existingItem.getItemId());
        itemResponseDTO.setItemName(existingItem.getItemName());
        itemResponseDTO.setItemDetails(existingItem.getItemDetails());
        itemResponseDTO.setItemType(existingItem.getItemType().name());
        itemResponseDTO.setCategory(category);
        itemResponseDTO.setImageUrl(existingItem.getImageUrl());
        itemResponseDTO.setPrice(priceDTO);

        return itemResponseDTO1;

    }

    @Override
    public List<ItemResponseDTO> getAllItems() {
        List<Item> itemList = itemRepository.findAll();

        return itemList.stream().map(x -> modelMapper.map(x, ItemResponseDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteItem(String itemId) {
        Optional<Item> optItem = itemRepository.findById(itemId);
        if (optItem.isEmpty()) {
            throw new NoElementFoundException("Item with given id is not present.", NULL_ITEM);
        }

        itemRepository.deleteById(itemId);
        evictCacheByName(itemId);
    }

    @Override
    @Cacheable(key = "#itemId")
    public ItemResponseDTO getItemByID(String itemId) {
        Optional<Item> optItem = itemRepository.findById(itemId);
        if (optItem.isEmpty()) {
            throw new NoElementFoundException("Item with given id is not present.", NULL_ITEM);
        }

        return modelMapper.map(optItem.get(), ItemResponseDTO.class);
    }

    @Override
    @Cacheable(key = "#itemName")
    public ItemResponseDTO getItemByItemName(String itemName) {
        Optional<Item> optionalItem = itemRepository.findByItemNameIgnoreCase(itemName);
        if (optionalItem.isEmpty()) {
            throw new NoElementFoundException("Item with given name is not present.", NULL_ITEM);
        }

        return modelMapper.map(optionalItem.get(), ItemResponseDTO.class);
    }

    private void evictCacheByName(String cacheKey) {
        Cache cache = cacheManager.getCache("items");
        if (cache != null) {
            log.info("evicting the {} cache", cacheKey);
            synchronized (ItemServiceImpl.class) {
                cache.evict(cacheKey);
            }
        }
    }

    @Override
    public List<ItemResponseDTO> getItemByCategoryName(String categoryName) {
        List<Item> itemList = itemRepository.findByCategory_CategoryName(categoryName);
        return itemList.stream().map(item -> modelMapper.map(item, ItemResponseDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDTO> getItemsByItemType(ItemType itemType) {
        if (!isValidItemType(itemType)) {
            throw new CustomIllegalException("Item Type is not valid.", null);
        }
        List<Item> itemList = itemRepository.findByItemType_(itemType);
        return itemList.stream().map(item -> modelMapper.map(item, ItemResponseDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDTO> getItemsByCategoryAndItemType(String categoryName, ItemType itemType) {
        if (!isValidItemType(itemType)) {
            throw new CustomIllegalException("Item Type is not valid.", null);
        }
        List<Item> itemList = itemRepository.findByCategory_CategoryNameAndItemType(categoryName, itemType);
        return itemList.stream().map(item -> modelMapper.map(item, ItemResponseDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDTO> getItemBySearchKeyword(String keyword) {
        String[] searchTerms = keyword.toLowerCase().split(" ");
        Set<Item> matchingItems = new HashSet<>();

        for (String term : searchTerms) {
            List<Item> items = itemRepository.getItemBySearchKeyword("%" + term + "%");
            matchingItems.addAll(items);
        }

        return matchingItems.stream().map(item -> modelMapper.map(item, ItemResponseDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDTO> getItemByDiscountPercent(Double discountPercentage) {
        String discountDetailsJson = "{\"discountPercentage\": " + discountPercentage + "}";
        List<Item> itemList = itemRepository.findByDiscountDetailsContaining(discountDetailsJson);

        return itemList.stream().map(item -> modelMapper.map(item, ItemResponseDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDTO> getItemsByPrice(Double itemPrice) {
        List<Item> itemList = itemRepository.findByDefaultPrice(itemPrice);

        return itemList.stream().map(item -> modelMapper.map(item, ItemResponseDTO.class)).collect(Collectors.toList());
    }

    private boolean isValidItemType(ItemType itemType) {
        return Arrays.stream(ItemType.values()).map(ItemType::name).collect(Collectors.toSet()).contains(itemType.name());
    }
}
