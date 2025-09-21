package com.civa.platform.fleet.interfaces.rest.transform;

import com.civa.platform.fleet.domain.model.valueobjects.BusBrandDependencies;
import com.civa.platform.fleet.interfaces.rest.resources.BusBrandDependenciesResource;

/**
 * Assembler to transform BusBrandDependencies domain value object to BusBrandDependenciesResource
 */
public class BusBrandDependenciesResourceFromValueObjectAssembler {
    
    /**
     * Transforms a BusBrandDependencies value object to a BusBrandDependenciesResource
     * @param dependencies The BusBrandDependencies value object from domain layer
     * @return The corresponding BusBrandDependenciesResource for the REST interface
     */
    public static BusBrandDependenciesResource toResourceFromValueObject(BusBrandDependencies dependencies) {
        return new BusBrandDependenciesResource(
                dependencies.busBrandId(),
                dependencies.brandName(),
                dependencies.activeBusesCount(),
                dependencies.inactiveBusesCount(),
                dependencies.totalBusesCount(),
                dependencies.canBeDeleted()
        );
    }
}