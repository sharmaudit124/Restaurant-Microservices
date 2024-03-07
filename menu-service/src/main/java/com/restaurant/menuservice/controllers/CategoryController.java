package com.restaurant.menuservice.controllers;

import com.restaurant.menuservice.dtos.CategoryDTO;
import com.restaurant.menuservice.dtos.CreateCategoryDTO;
import com.restaurant.menuservice.entities.response.MenuResponse;
import com.restaurant.menuservice.services.CategoryServiceImpl;
import com.restaurant.menuservice.utils.SuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cat")
public class CategoryController {

    private final CategoryServiceImpl categoryService;
    private final SuccessResponse successResponse;

    /**
     * Constructs a new CategoryController with the given CategoryService and SuccessResponse instances.
     *
     * @param categoryService The service responsible for category-related operations.
     * @param successResponse The success response utility for generating HTTP responses.
     */
    @Autowired
    public CategoryController(CategoryServiceImpl categoryService, SuccessResponse successResponse) {
        this.categoryService = categoryService;
        this.successResponse = successResponse;
    }

    /**
     * Retrieves a list of all categories.
     *
     * @return ResponseEntity containing a MenuResponse with a list of CategoryDTOs.
     */
    @GetMapping
    public ResponseEntity<MenuResponse<List<CategoryDTO>>> getCategoriesList() {
        log.info("received request to retrieve list  of categories.");
        ResponseEntity<MenuResponse<List<CategoryDTO>>> response = successResponse.getResponse(categoryService.findAllCategories(), HttpStatus.OK);
        log.info("returning list of size {}.", response.getBody().getResponse().size());
        return response;
    }

    /**
     * Retrieves details of a category by its name.
     *
     * @param categoryName The name of the category to retrieve.
     * @return ResponseEntity containing a MenuResponse with the details of the requested CategoryDTO.
     */
    @GetMapping("/{categoryName}")
    public ResponseEntity<MenuResponse<CategoryDTO>> getCategoryByName(@PathVariable String categoryName) {
        log.info("received request to retrieve details of {}.", categoryName);
        ResponseEntity<MenuResponse<CategoryDTO>> response = successResponse.getResponse(categoryService.findCategoryByCategoryName(categoryName), HttpStatus.OK);
        log.info("returning details of {}.", categoryName);
        return response;
    }

    /**
     * Creates a new category.
     *
     * @param createCategoryDTO The data necessary to create a new category.
     * @return ResponseEntity containing a MenuResponse with the newly created CategoryDTO.
     */
    @PostMapping("/admin")
    public ResponseEntity<MenuResponse<CategoryDTO>> createNewCategory(@RequestBody CreateCategoryDTO createCategoryDTO) {
        log.info("received request to add new category as {}.", createCategoryDTO.getCategoryName());
        ResponseEntity<MenuResponse<CategoryDTO>> response = successResponse.getResponse(categoryService.addCategory(createCategoryDTO), HttpStatus.CREATED);
        log.info("{} successfully added", createCategoryDTO.getCategoryName());
        return response;
    }

    /**
     * Updates the details of an existing category.
     *
     * @param categoryDTO The updated details of the category.
     * @return ResponseEntity containing a MenuResponse with the updated CategoryDTO.
     */
    @PutMapping("/admin")
    public ResponseEntity<MenuResponse<CategoryDTO>> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("received request to update details of {}.", categoryDTO.getCategoryId());
        ResponseEntity<MenuResponse<CategoryDTO>> response = successResponse.getResponse(categoryService.updateCategory(categoryDTO), HttpStatus.ACCEPTED);
        log.info("{} successfully updated to", categoryDTO.getCategoryName());
        return response;
    }

    /**
     * Deletes a category by its name.
     *
     * @param categoryName The name of the category to be deleted.
     * @return ResponseEntity containing a MenuResponse indicating a successful deletion.
     */
    @DeleteMapping("/admin/{categoryName}")
    public ResponseEntity<MenuResponse<CategoryDTO>> deleteCategory(@PathVariable String categoryName) {
        log.warn("received request to delete {} category.", categoryName);
        categoryService.deleteCategoryByName(categoryName);
        ResponseEntity<MenuResponse<CategoryDTO>> response = successResponse.getResponse(null, HttpStatus.OK);
        log.info("{} deleted successfully.", categoryName);
        return response;
    }
}
