package com.civa.platform.fleet.interfaces.rest.resources;

/**
 * Resource for creating a new bus
 * @param number Bus number (required)
 * @param licensePlate License plate (required)
 * @param brandId Bus brand ID (required)
 * @param features Bus features (required)
 * @param status Bus status (optional, defaults to ACTIVE)
 */
public record CreateBusResource(String number, String licensePlate, Long brandId, String features, String status) {
}
