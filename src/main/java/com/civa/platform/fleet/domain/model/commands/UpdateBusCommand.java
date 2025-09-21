package com.civa.platform.fleet.domain.model.commands;

public record UpdateBusCommand(Long busId, String number, Long brandId, String licensePlate, String features, String status) {
    
    /**
     * Constructor with minimal required fields for update
     */
    public UpdateBusCommand(Long busId, String number, Long brandId, String licensePlate, String features) {
        this(busId, number, brandId, licensePlate, features, null);
    }
}