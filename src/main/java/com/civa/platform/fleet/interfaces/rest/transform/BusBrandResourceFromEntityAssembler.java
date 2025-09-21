package com.civa.platform.fleet.interfaces.rest.transform;

import com.civa.platform.fleet.domain.model.entities.BusBrand;
import com.civa.platform.fleet.interfaces.rest.resources.BusBrandResource;

public class BusBrandResourceFromEntityAssembler {
    public static BusBrandResource toResourceFromEntity(BusBrand busBrand) {
        return new BusBrandResource(
                busBrand.getId(),
                busBrand.getName().name()
        );
    }
}
