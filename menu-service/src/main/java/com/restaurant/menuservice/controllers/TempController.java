package com.restaurant.menuservice.controllers;

import com.restaurant.menuservice.entities.Category;
import com.restaurant.menuservice.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TempController {
    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("/allCat")
    public List<Category> saveALlCat(@RequestBody List<Category> ls) {
        return categoryRepository.saveAll(ls);
    }
}
