package com.civa.platform.fleet.interfaces.rest.transform;

import com.civa.platform.fleet.domain.model.agregates.Bus;
import com.civa.platform.fleet.interfaces.rest.resources.BusResource;

public class BusResourceFromEntityAssembler {
    public static BusResource toResourceFromEntity(Bus bus) {
        return new BusResource(
                bus.getId(),
                bus.getNumber().number(),
                bus.getLicensePlate().plate(),
                bus.getBusBrand().getName().name(),
                bus.getFeatures().features(), // This can now be null
                bus.getStatus().name()
        );
    }
}
