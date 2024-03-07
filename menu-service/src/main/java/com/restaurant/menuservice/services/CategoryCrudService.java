package com.restaurant.menuservice.services;

import com.restaurant.menuservice.dtos.CategoryDTO;
import com.restaurant.menuservice.dtos.CreateCategoryDTO;

import java.util.List;

public interface CategoryCrudService {
    /**
     * to add a new category to database
     *
     * @param createCategoryDTO
     * @return
     */
    public CategoryDTO addCategory(CreateCategoryDTO createCategoryDTO);

    /**
     * to update the previous category present
     *
     * @param categoryDTO
     * @return
     */
    public CategoryDTO updateCategory(CategoryDTO categoryDTO);

    /**
     * to find category by category name
     *
     * @param categoryName
     * @return
     */
    public CategoryDTO findCategoryByCategoryName(String categoryName);

    /**
     * to retrieve list of all present categories
     *
     * @return
     */
    public List<CategoryDTO> findAllCategories();

    /**
     * to delete categories by categoryName
     *
     * @param categoryName
     */
    public void deleteCategoryByName(String categoryName);
}
