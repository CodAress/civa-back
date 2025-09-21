package com.civa.platform.fleet.interfaces.rest.transform;

import com.civa.platform.fleet.domain.model.commands.CreateBusCommand;
import com.civa.platform.fleet.interfaces.rest.resources.CreateBusResource;

public class CreateBusCommandFromResourceAssembler {
    public static CreateBusCommand toCommandFromResource(CreateBusResource resource) {
        return new CreateBusCommand(
                resource.number(),
                resource.brandId(),
                resource.licensePlate(),
                resource.features(),
                resource.status()
        );
    }
}
