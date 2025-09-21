package com.civa.platform.fleet.domain.model.valueobjects;

public enum BusStatus {
    ACTIVE, INACTIVE;

    public static boolean isValidEnum(String status) {
        try {
            BusStatus.valueOf(status.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static BusStatus fromString(String status) {
        if (isValidEnum(status)) {
            return BusStatus.valueOf(status.toUpperCase());
        } else {
            throw new IllegalArgumentException("Invalid bus status: " + status);
        }
    }
}