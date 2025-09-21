package com.civa.platform.shared.domain.model.valueobjects;

/**
 * Represents a sort order for pagination
 */
public record SortOrder(
        String property,
        Direction direction
) {
    public static SortOrder asc(String property) {
        return new SortOrder(property, Direction.ASC);
    }
    
    public static SortOrder desc(String property) {
        return new SortOrder(property, Direction.DESC);
    }
    
    public enum Direction {
        ASC, DESC
    }
}