package com.civa.platform.fleet.domain.model.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateBusBrandCommand(
        @NotNull(message = "Brand ID is required")
        Long brandId,

        @NotBlank(message = "Brand name is required")
        String name
) {
}