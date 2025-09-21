package com.civa.platform.fleet.domain.model.commands;

import com.civa.platform.fleet.domain.model.valueobjects.BusStatus;

public record CreateBusCommand(String number, Long brandId, String licensePlate, String features, String status) {
    
    // Constructor que usa ACTIVE por defecto
    public CreateBusCommand(String number, Long brandId, String licensePlate, String features) {
        this(number, brandId, licensePlate, features, BusStatus.ACTIVE.name());
    }
    
    // Método para obtener el status, garantizando que nunca sea null
    public String getStatusOrDefault() {
        return status != null ? status : BusStatus.ACTIVE.name();
    }
}
