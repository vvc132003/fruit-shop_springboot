package com.fruitshop.fruit_shop.dto.response;

public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private Meta meta;

    public ApiResponse(boolean success, String message, T data, Meta meta) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.meta = meta;
    }

    // SUCCESS (có data + meta)
    public static <T> ApiResponse<T> success(String message, T data, Meta meta) {
        return new ApiResponse<>(true, message, data, meta);
    }

    // SUCCESS (chỉ data)
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, null);
    }

    // ERROR
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }
}