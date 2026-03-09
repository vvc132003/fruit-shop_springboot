package com.fruitshop.fruit_shop.controllerAPI;

import com.fruitshop.fruit_shop.entity.Category;
import com.fruitshop.fruit_shop.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryApiController {

    private final CategoryService categoryService;

    public CategoryApiController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // GET ALL CATEGORY
    @GetMapping
    public List<Category> getAll() {
        return categoryService.getAll();
    }

    // CREATE CATEGORY
    @PostMapping
    public Category create(@RequestBody Category category) {
        return categoryService.save(category);
    }

    // DELETE CATEGORY
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        categoryService.delete(id);
    }
}