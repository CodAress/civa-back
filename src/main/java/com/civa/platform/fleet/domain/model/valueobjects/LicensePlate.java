package com.civa.platform.fleet.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record LicensePlate(String plate) {
    public LicensePlate {
        if (plate == null || plate.isBlank()) {
            throw new IllegalArgumentException("License plate cannot be null or empty");
        }
    }
}
