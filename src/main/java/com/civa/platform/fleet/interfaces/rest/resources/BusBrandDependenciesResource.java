package com.civa.platform.fleet.interfaces.rest.resources;

public record BusBrandDependenciesResource(
    Long busBrandId,
    String brandName,
    Long activeBusesCount,
    Long inactiveBusesCount,
    Long totalBusesCount,
    boolean canBeDeleted
) {
}