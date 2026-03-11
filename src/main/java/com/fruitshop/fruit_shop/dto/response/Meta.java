package com.fruitshop.fruit_shop.dto.response;

public class Meta {

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private int currentElements;

    public Meta(int page, int size, long totalElements, int totalPages, int currentElements) {
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentElements = currentElements;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentElements() {
        return currentElements;
    }
}