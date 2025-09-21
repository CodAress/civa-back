package com.civa.platform.shared.domain.model.valueobjects;

import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Represents a page request with pagination parameters
 */
public record PageRequest(
        int page,
        int size,
        List<SortOrder> sortOrders
) {
    public PageRequest {
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be positive");
        }
        if (size > 100) {
            throw new IllegalArgumentException("Page size cannot exceed 100");
        }
    }

    /**
     * Constructor without sorting
     */
    public PageRequest(int page, int size) {
        this(page, size, List.of());
    }

    /**
     * Creates a default page request (page 0, size 20)
     */
    public static PageRequest defaultPage() {
        return new PageRequest(0, 20, List.of());
    }

    /**
     * Creates a page request with specified page and default size (20)
     */
    public static PageRequest of(int page) {
        return new PageRequest(page, 20, List.of());
    }

    /**
     * Creates a page request with specified page and size
     */
    public static PageRequest of(int page, int size) {
        return new PageRequest(page, size, List.of());
    }

    /**
     * Creates a page request with specified page, size and sorting
     */
    public static PageRequest of(int page, int size, List<SortOrder> sortOrders) {
        return new PageRequest(page, size, sortOrders);
    }

    /**
     * Creates a page request with specified page, size and single sort order
     */
    public static PageRequest of(int page, int size, SortOrder sortOrder) {
        return new PageRequest(page, size, List.of(sortOrder));
    }

    /**
     * Converts to Spring Data Pageable
     */
    public org.springframework.data.domain.Pageable toPageable() {
        if (sortOrders == null || sortOrders.isEmpty()) {
            return org.springframework.data.domain.PageRequest.of(page, size);
        }
        
        Sort sort = Sort.by(
            sortOrders.stream()
                .map(order -> order.direction() == SortOrder.Direction.ASC 
                    ? Sort.Order.asc(order.property())
                    : Sort.Order.desc(order.property()))
                .toList()
        );
        
        return org.springframework.data.domain.PageRequest.of(page, size, sort);
    }
}