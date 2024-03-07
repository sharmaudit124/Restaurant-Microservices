package com.restaurant.menuservice.repositories;

import com.restaurant.menuservice.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    public Optional<Category> findByCategoryNameIgnoreCase(String categoryName);

    void deleteByCategoryName(String categoryName);
}
