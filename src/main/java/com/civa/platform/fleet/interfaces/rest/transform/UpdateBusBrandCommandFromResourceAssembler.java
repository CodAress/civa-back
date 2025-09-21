package com.civa.platform.fleet.interfaces.rest.transform;

import com.civa.platform.fleet.domain.model.commands.UpdateBusBrandCommand;
import com.civa.platform.fleet.interfaces.rest.resources.UpdateBusBrandResource;

public class UpdateBusBrandCommandFromResourceAssembler {

    public static UpdateBusBrandCommand toCommandFromResource(Long brandId, UpdateBusBrandResource resource) {
        return new UpdateBusBrandCommand(
                brandId,
                resource.name()
        );
    }
}