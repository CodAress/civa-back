package com.civa.platform.fleet.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record BusNumber(String number) {
    public BusNumber {
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("Bus number cannot be null or empty");
        }
    }
}
