package com.civa.platform.fleet.interfaces.rest.transform;

import com.civa.platform.fleet.domain.model.commands.UpdateBusCommand;
import com.civa.platform.fleet.interfaces.rest.resources.UpdateBusResource;

public class UpdateBusCommandFromResourceAssembler {

    public static UpdateBusCommand toCommandFromResource(Long busId, UpdateBusResource resource) {
        return new UpdateBusCommand(
                busId,
                resource.number(),
                resource.brandId(),
                resource.licensePlate(),
                resource.features(),
                resource.status()
        );
    }
}