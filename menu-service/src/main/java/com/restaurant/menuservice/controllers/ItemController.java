package com.restaurant.menuservice.controllers;

import com.restaurant.menuservice.dtos.ItemRequestDTO;
import com.restaurant.menuservice.dtos.ItemResponseDTO;
import com.restaurant.menuservice.entities.response.MenuResponse;
import com.restaurant.menuservice.enums.ItemType;
import com.restaurant.menuservice.services.ItemServiceImpl;
import com.restaurant.menuservice.utils.SuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/item")
public class ItemController {
    private final ItemServiceImpl itemService;

    private final SuccessResponse successResponse;

    @Autowired
    public ItemController(ItemServiceImpl itemService, SuccessResponse successResponse) {
        this.itemService = itemService;
        this.successResponse = successResponse;
    }

    @PostMapping("/admin")
    public ResponseEntity<MenuResponse<ItemResponseDTO>> addItem(@RequestBody ItemRequestDTO itemRequestDTO) {
        ResponseEntity<MenuResponse<ItemResponseDTO>> response = successResponse.getResponse(itemService.createItem(itemRequestDTO), HttpStatus.CREATED);

        return response;
    }

    @PutMapping("/admin")
    public ResponseEntity<MenuResponse<ItemResponseDTO>> updateItem(@RequestBody ItemResponseDTO itemResponseDTO) {
        ResponseEntity<MenuResponse<ItemResponseDTO>> response = successResponse.getResponse(itemService.updateItem(itemResponseDTO), HttpStatus.ACCEPTED);
        return response;
    }

    @DeleteMapping("/admin")
    public ResponseEntity<MenuResponse<ItemResponseDTO>> deleteItem(@PathVariable String itemId) {
        itemService.deleteItem(itemId);
        return successResponse.getResponse(null, HttpStatus.OK);
    }

    @GetMapping("/all-items")
    public ResponseEntity<MenuResponse<List<ItemResponseDTO>>> findAll() {
        ResponseEntity<MenuResponse<List<ItemResponseDTO>>> response = successResponse.getResponse(itemService.getAllItems(), HttpStatus.OK);
        return response;
    }

    @GetMapping
    public ResponseEntity<MenuResponse<ItemResponseDTO>> getItemById(@PathVariable String itemId) {
        ResponseEntity<MenuResponse<ItemResponseDTO>> response = successResponse.getResponse(itemService.getItemByID(itemId), HttpStatus.OK);
        return response;
    }

    @GetMapping("/all-items/categories")
    public ResponseEntity<MenuResponse<List<ItemResponseDTO>>> findAllByCategories(@RequestParam(name = "categoryName") String categoryName) {
        ResponseEntity<MenuResponse<List<ItemResponseDTO>>> response = successResponse.getResponse(itemService.getItemByCategoryName((categoryName)), HttpStatus.OK);
        return response;
    }

    @GetMapping("/all-items/item-type")
    public ResponseEntity<MenuResponse<List<ItemResponseDTO>>> findAllByItemType(@RequestParam(name = "itemType") ItemType itemType) {
        ResponseEntity<MenuResponse<List<ItemResponseDTO>>> response = successResponse.getResponse(itemService.getItemsByItemType((itemType)), HttpStatus.OK);
        return response;
    }
}
