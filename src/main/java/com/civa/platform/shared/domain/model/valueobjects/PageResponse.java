package com.civa.platform.shared.domain.model.valueobjects;

import java.util.List;

/**
 * Represents a paginated response containing data and pagination metadata
 */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last,
        boolean empty
) {
    /**
     * Creates a PageResponse from Spring Data Page
     */
    public static <T> PageResponse<T> from(org.springframework.data.domain.Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.isEmpty()
        );
    }

    /**
     * Creates an empty PageResponse
     */
    public static <T> PageResponse<T> empty(PageRequest pageRequest) {
        return new PageResponse<>(
                List.of(),
                pageRequest.page(),
                pageRequest.size(),
                0L,
                0,
                true,
                true,
                true
        );
    }

    /**
     * Maps the content to a different type using the provided mapper function
     */
    public <U> PageResponse<U> map(java.util.function.Function<T, U> mapper) {
        List<U> mappedContent = content.stream()
                .map(mapper)
                .toList();
        
        return new PageResponse<>(
                mappedContent,
                page,
                size,
                totalElements,
                totalPages,
                first,
                last,
                empty
        );
    }
}