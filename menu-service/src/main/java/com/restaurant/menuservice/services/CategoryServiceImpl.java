package com.restaurant.menuservice.services;

import com.restaurant.menuservice.dtos.CategoryDTO;
import com.restaurant.menuservice.dtos.CreateCategoryDTO;
import com.restaurant.menuservice.entities.Category;
import com.restaurant.menuservice.exceptions.ElementAlreadyExistException;
import com.restaurant.menuservice.exceptions.NoElementFoundException;
import com.restaurant.menuservice.repositories.CategoryRepository;
import com.restaurant.menuservice.utils.UUIDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@CacheConfig(cacheNames = {"categories"})
public class CategoryServiceImpl implements CategoryCrudService {
    public static final CategoryDTO NULL_CATEGORY = new CategoryDTO();
    private final CategoryRepository categoryRepository;
    private final UUIDGenerator uuidGenerator;
    private final CacheManager cacheManager;
    private final ModelMapper modelMapper;

    /**
     * Constructor for the CategoryService implementation.
     *
     * @param categoryRepository The repository for accessing category data from the database.
     * @param modelMapper        Utility for mapping between DTOs and entities.
     * @param uuidGenerator      Generator for creating unique identifiers (UUIDs).
     * @param cacheManager       CacheManager for caching and managing cached data.
     */
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, UUIDGenerator uuidGenerator, CacheManager cacheManager) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.uuidGenerator = uuidGenerator;
        this.cacheManager = cacheManager;
    }

    /**
     * Adds a new category based on the provided CreateCategoryDTO.
     *
     * @param createCategoryDTO The DTO containing category details to be added.
     * @return The DTO representation of the added category.
     * @throws ElementAlreadyExistException If a category with the same name already exists.
     */
    @Override
    @Transactional
    public CategoryDTO addCategory(CreateCategoryDTO createCategoryDTO) {
        Optional<Category> categoryName = categoryRepository.findByCategoryNameIgnoreCase(createCategoryDTO.getCategoryName());
        if (categoryName.isPresent()) {
            throw new ElementAlreadyExistException("given resource " + categoryName.get().getCategoryName() + " already exists.", NULL_CATEGORY);
        }

        Category category = new Category();
        category.setCategoryId(uuidGenerator.generateUUIDString());
        category.setCategoryName(createCategoryDTO.getCategoryName());
        log.info("saving category in database");
        categoryRepository.save(category);

        return modelMapper.map(category, CategoryDTO.class);
    }

    /**
     * Updates an existing category based on the provided CategoryDTO.
     *
     * @param categoryDTO The DTO containing updated category details.
     * @return The DTO representation of the updated category.
     * @throws NoElementFoundException If the category to be updated is not found.
     */
    @Override
    @Transactional
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        Optional<Category> category = categoryRepository.findById(categoryDTO.getCategoryId());

        if (category.isEmpty()) {
            throw new NoElementFoundException("resource with given id " + categoryDTO.getCategoryId() + " not found", NULL_CATEGORY);
        }

        evictCacheByName(category.get().getCategoryName());

        Category categoryToUpdate = new Category();
        categoryToUpdate.setCategoryId(categoryDTO.getCategoryId());
        categoryToUpdate.setCategoryName(categoryDTO.getCategoryName());
        log.info("updating {} category name in database", categoryDTO.getCategoryId());
        categoryRepository.save(categoryToUpdate);

        return modelMapper.map(categoryToUpdate, CategoryDTO.class);
    }

    /**
     * Retrieves category details by its name.
     *
     * @param categoryName The name of the category to retrieve.
     * @return The DTO representation of the retrieved category.
     * @throws NoElementFoundException If the category with the specified name not exists.
     */
    @Override
    @Cacheable(key = "#categoryName")
    @Transactional
    public CategoryDTO findCategoryByCategoryName(String categoryName) {
        log.info("extracting {}'s details from database.", categoryName);
        Optional<Category> optCategory = categoryRepository.findByCategoryNameIgnoreCase(categoryName);
        if (optCategory.isEmpty()) {
            throw new NoElementFoundException("resource with given category " + categoryName + " not found", NULL_CATEGORY);
        }

        return modelMapper.map(optCategory.get(), CategoryDTO.class);
    }

    /**
     * Retrieves a list of all categories.
     *
     * @return A list of DTO representations of all categories.
     */
    @Override
    @Transactional
    public List<CategoryDTO> findAllCategories() {
        List<Category> categoryList = categoryRepository.findAll();

        log.info("extracted {} categories from database.", categoryList.size());

        Type listType = new TypeToken<List<CategoryDTO>>() {
        }.getType();

        return modelMapper.map(categoryList, listType);
    }

    /**
     * Deletes a category by its name.
     *
     * @param categoryName The name of the category to delete.
     * @throws NoElementFoundException If the category to be deleted is not found.
     */
    @Override
    @Transactional
    public void deleteCategoryByName(String categoryName) {
        Optional<Category> category = categoryRepository.findByCategoryNameIgnoreCase(categoryName);

        if (category.isEmpty()) {
            throw new NoElementFoundException("resource with given name " + categoryName + " not found", NULL_CATEGORY);
        }
        evictCacheByName(categoryName);
        log.warn("deleting {} category from database.", categoryName);
        categoryRepository.deleteByCategoryName(categoryName);
    }

    /**
     * Evicts the cache entry associated with the specified category name.
     *
     * @param categoryName The name of the category to evict from the cache.
     */
    private void evictCacheByName(String categoryName) {
        Cache cache = cacheManager.getCache("categories");
        if (cache != null) {
            log.info("evicting the {} cache", categoryName);
            cache.evict(categoryName);
        }
    }
}
