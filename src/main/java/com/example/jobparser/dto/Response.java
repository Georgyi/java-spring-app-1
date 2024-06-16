package com.example.jobparser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Response<T> {
    private T data;
    private String message;
    private Integer code;

    public static <T> Response<T> generate(T data, String message, Integer code) {
        return Response.<T>builder()
                .data(data)
                .message(message)
                .code(code)
                .build();
    }
}
