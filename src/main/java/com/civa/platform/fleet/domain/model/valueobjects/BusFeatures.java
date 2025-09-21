package com.civa.platform.fleet.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record BusFeatures(String features) {
    
    public BusFeatures {
        if (features == null || features.trim().isEmpty()) {
            throw new IllegalArgumentException("Bus features cannot be null or empty");
        }
    }
    
    /**
     * Factory method for creating BusFeatures with validation
     */
    public static BusFeatures of(String features) {
        return new BusFeatures(features);
    }
}
