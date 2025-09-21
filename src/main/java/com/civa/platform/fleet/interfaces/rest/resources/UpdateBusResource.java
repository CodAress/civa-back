package com.civa.platform.fleet.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateBusResource(
        @NotBlank(message = "Bus number is required")
        String number,

        @NotNull(message = "Brand ID is required")
        Long brandId,

        @NotBlank(message = "License plate is required")
        String licensePlate,

        @NotBlank(message = "Features are required")
        String features,

        String status // Optional - puede ser null
) {
}