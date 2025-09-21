package com.civa.platform.fleet.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record BrandName(String name) {
    public BrandName {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Brand name cannot be null or empty");
        }
    }
}
