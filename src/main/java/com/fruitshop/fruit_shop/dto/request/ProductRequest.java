package com.fruitshop.fruit_shop.dto.request;

import com.fruitshop.fruit_shop.entity.Product.ProductStatus;

public class ProductRequest {

    private String name;
    private Double price;
    private String description;
    private String image;
    private String unit;
    private Integer stock;
    private ProductStatus status;
    private Boolean isFeatured;
    private Integer categoryId;

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getUnit() {
        return unit;
    }

    public Integer getStock() {
        return stock;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}