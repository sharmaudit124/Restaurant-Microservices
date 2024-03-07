package com.restaurant.menuservice.services;

import com.restaurant.menuservice.dtos.ItemRequestDTO;
import com.restaurant.menuservice.dtos.ItemResponseDTO;

import java.util.List;

public interface ItemCrudService {
    /**
     * method to create Item for a given category
     *
     * @param itemRequestDTO
     * @return
     */
    public ItemResponseDTO createItem(ItemRequestDTO itemRequestDTO);

    /**
     * to update Item based on given itemId if exist
     *
     * @param itemResponseDTO
     * @return
     */
    public ItemResponseDTO updateItem(ItemResponseDTO itemResponseDTO);

    /**
     * to get list of all items
     *
     * @return
     */
    public List<ItemResponseDTO> getAllItems();

    /**
     * to delete Item based on itemId
     *
     * @param itemId
     */
    public void deleteItem(String itemId);

    /**
     * to get item by itemId
     *
     * @param itemId
     * @return
     */
    public ItemResponseDTO getItemByID(String itemId);

    /**
     * to get item by itemName
     *
     * @param itemName
     * @return
     */
    public ItemResponseDTO getItemByItemName(String itemName);


}
