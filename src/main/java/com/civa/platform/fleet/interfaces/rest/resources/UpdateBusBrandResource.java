package com.civa.platform.fleet.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record UpdateBusBrandResource(
        @NotBlank(message = "Brand name is required")
        String name
) {
}