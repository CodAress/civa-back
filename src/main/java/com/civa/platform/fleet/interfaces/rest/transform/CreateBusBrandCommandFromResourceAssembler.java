package com.civa.platform.fleet.interfaces.rest.transform;

import com.civa.platform.fleet.domain.model.commands.CreateBusBrandCommand;
import com.civa.platform.fleet.interfaces.rest.resources.CreateBusBrandResource;

public class CreateBusBrandCommandFromResourceAssembler {
    public static CreateBusBrandCommand toCommandFromResource(CreateBusBrandResource resource) {
        return new CreateBusBrandCommand(resource.name());
    }
}
