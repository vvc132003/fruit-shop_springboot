package com.fruitshop.fruit_shop.mapper;

import com.fruitshop.fruit_shop.dto.request.ProductRequest;
import com.fruitshop.fruit_shop.dto.response.ProductResponse;
import com.fruitshop.fruit_shop.entity.Category;
import com.fruitshop.fruit_shop.entity.Product;

public class ProductMapper {

    public static Product toEntity(ProductRequest request, Category category) {

        Product product = new Product();

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setImage(request.getImage());
        product.setUnit(request.getUnit());
        product.setStock(request.getStock());
        product.setStatus(request.getStatus());
        product.setIsFeatured(request.getIsFeatured());
        product.setCategory(category);

        return product;
    }

    public static ProductResponse toResponse(Product product) {

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getImage(),
                product.getUnit(),
                product.getStock(),
                product.getStatus().name(),
                product.getIsFeatured(),
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }
}