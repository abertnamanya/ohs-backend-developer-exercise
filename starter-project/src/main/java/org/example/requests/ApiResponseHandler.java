package org.example.requests;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseHandler<T> {
    private String message;
    private T data;


    public ApiResponseHandler(String message) {
        this.message = message;
    }

    public ApiResponseHandler(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public ApiResponseHandler(T data) {
        this.data = data;
    }


    public static <T> ApiResponseHandler<T> of(String message, T data) {
        return new ApiResponseHandler<>(message, data);
    }

    public static <T> ApiResponseHandler<T> of(String message) {
        return new ApiResponseHandler<>(message);
    }

    public static <T> ApiResponseHandler<T> of(T data) {
        return new ApiResponseHandler<>(data);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
