package com.example.jobparser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PaginatedResponse<T> {
    private List<T> list;
    private Integer page;
    private Integer size;
    private Long totalSize;
    private Integer pages;

    public static <T> PaginatedResponse<T> transform(Page<T> page) {
        return PaginatedResponse.<T>builder()
                .list(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalSize(page.getTotalElements())
                .pages(page.getTotalPages())
                .build();
    }
}
