package com.restaurant.menuservice.services;

import com.restaurant.menuservice.dtos.ItemResponseDTO;
import com.restaurant.menuservice.enums.ItemType;

import java.util.List;

public interface ItemConditionService {
    /**
     * to get list of items having same category
     *
     * @param categoryName
     * @return
     */
    public List<ItemResponseDTO> getItemByCategoryName(String categoryName);

    /**
     * to get list of items having an itemType
     *
     * @param itemType
     * @return
     */
    public List<ItemResponseDTO> getItemsByItemType(ItemType itemType);

    /**
     * to get list of items for given category and itemType
     *
     * @param Category
     * @param itemType
     * @return
     */
    public List<ItemResponseDTO> getItemsByCategoryAndItemType(String categoryName, ItemType itemType);

    /**
     * to get list of items based on search keyword
     *
     * @param keyword
     * @return
     */
    public List<ItemResponseDTO> getItemBySearchKeyword(String keyword);

    /**
     * to get list of items based on DiscountPercent
     *
     * @param discountPercentage
     * @return
     */
    public List<ItemResponseDTO> getItemByDiscountPercent(Double discountPercentage);

    /**
     * to get list of items based on price
     * @param itemPrice
     * @return
     */
    public List<ItemResponseDTO> getItemsByPrice(Double itemPrice);
}
