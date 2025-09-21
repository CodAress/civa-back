package com.civa.platform.fleet.domain.model.agregates;

import com.civa.platform.fleet.domain.model.entities.BusBrand;
import com.civa.platform.fleet.domain.model.valueobjects.BusFeatures;
import com.civa.platform.fleet.domain.model.valueobjects.BusNumber;
import com.civa.platform.fleet.domain.model.valueobjects.BusStatus;
import com.civa.platform.fleet.domain.model.valueobjects.LicensePlate;
import com.civa.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Bus extends AuditableAbstractAggregateRoot<Bus> {

    @Embedded
    private BusNumber number;

    @Embedded
    private LicensePlate licensePlate;

    @Embedded
    private BusFeatures features;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private BusBrand busBrand;

    private BusStatus status;

    public Bus() {
        this.status = BusStatus.ACTIVE; // Status por defecto
    }

    public Bus(BusNumber number, LicensePlate licensePlate, BusBrand busBrand, BusFeatures features, BusStatus status) {
        this.number = number;
        this.licensePlate = licensePlate;
        this.busBrand = busBrand;
        this.features = features;
        this.status = status != null ? status : BusStatus.ACTIVE; // Si no se especifica, usar ACTIVE
    }

    public Bus(BusNumber number, LicensePlate licensePlate, BusBrand busBrand, BusFeatures features) {
        this.number = number;
        this.licensePlate = licensePlate;
        this.busBrand = busBrand;
        this.features = features;
        this.status = BusStatus.ACTIVE; // Constructor sin status, por defecto ACTIVE
    }

    // Métodos para cambiar el status
    public void activate() {
        this.status = BusStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = BusStatus.INACTIVE;
    }

    public boolean isActive() {
        return this.status == BusStatus.ACTIVE;
    }

    // Métodos para actualizar datos del bus
    public void updateDetails(BusNumber number, LicensePlate licensePlate, BusBrand busBrand, BusFeatures features) {
        this.number = number;
        this.licensePlate = licensePlate;
        this.busBrand = busBrand;
        this.features = features;
    }

    public void updateStatus(BusStatus status) {
        this.status = status != null ? status : BusStatus.ACTIVE;
    }

}
