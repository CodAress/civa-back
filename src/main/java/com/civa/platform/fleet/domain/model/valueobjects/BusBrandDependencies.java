package com.civa.platform.fleet.domain.model.valueobjects;

/**
 * Value Object representing bus brand dependencies information.
 * Contains counts of buses associated with a specific bus brand.
 */
public record BusBrandDependencies(
    Long busBrandId,
    String brandName,
    Long activeBusesCount,
    Long inactiveBusesCount,
    Long totalBusesCount,
    boolean canBeDeleted
) {
    public BusBrandDependencies {
        if (busBrandId == null || busBrandId <= 0) {
            throw new IllegalArgumentException("Bus brand ID must be positive");
        }
        if (brandName == null || brandName.isBlank()) {
            throw new IllegalArgumentException("Brand name cannot be null or empty");
        }
        if (activeBusesCount == null || activeBusesCount < 0) {
            throw new IllegalArgumentException("Active buses count cannot be negative");
        }
        if (inactiveBusesCount == null || inactiveBusesCount < 0) {
            throw new IllegalArgumentException("Inactive buses count cannot be negative");
        }
        if (totalBusesCount == null || totalBusesCount < 0) {
            throw new IllegalArgumentException("Total buses count cannot be negative");
        }
    }
}