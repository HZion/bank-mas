package com.sion.javamsauser.model.ResDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String redirectUrl;
    private String message;

    public static <T> ApiResponse<T> success(T data, String redirectUrl) {
        return new ApiResponse<>(true, data, redirectUrl, null);
    }

    public static <T> ApiResponse<T> error(String message, String redirectUrl) {
        return new ApiResponse<>(false, null, redirectUrl, message);
    }
}