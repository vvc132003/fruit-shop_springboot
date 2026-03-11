package com.fruitshop.fruit_shop.mapper;

import com.fruitshop.fruit_shop.dto.request.CategoryRequest;
import com.fruitshop.fruit_shop.dto.response.CategoryResponse;
import com.fruitshop.fruit_shop.entity.Category;

import java.text.Normalizer;

public class CategoryMapper {

    public static Category toEntity(CategoryRequest request) {

        Category category = new Category();

        category.setName(request.getName());

        category.setSlug(toSlug(request.getName()));

        return category;
    }

    public static CategoryResponse toResponse(Category category) {

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug()
        );
    }

    // ======================
    // Tạo slug
    // ======================
    public static String toSlug(String input) {

        String slug = Normalizer.normalize(input, Normalizer.Form.NFD);

        slug = slug.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        slug = slug.toLowerCase();

        slug = slug.replaceAll("[^a-z0-9]+", "-");

        slug = slug.replaceAll("^-|-$", "");

        return slug;
    }
}