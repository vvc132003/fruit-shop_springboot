package com.fruitshop.fruit_shop.dto.response;

public class ProductResponse {

    private Integer id;
    private String name;
    private Double price;
    private String description;
    private String image;
    private String unit;
    private Integer stock;
    private String status;
    private Boolean isFeatured;
    private Integer categoryId;
    private String categoryName;

    public ProductResponse(
            Integer id,
            String name,
            Double price,
            String description,
            String image,
            String unit,
            Integer stock,
            String status,
            Boolean isFeatured,
            Integer categoryId,
            String categoryName) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.unit = unit;
        this.stock = stock;
        this.status = status;
        this.isFeatured = isFeatured;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Integer getId() {
        return id;
    }

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

    public String getStatus() {
        return status;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}