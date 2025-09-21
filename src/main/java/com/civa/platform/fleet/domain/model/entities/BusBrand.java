package com.civa.platform.fleet.domain.model.entities;

import com.civa.platform.fleet.domain.model.valueobjects.BrandName;
import com.civa.platform.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class BusBrand extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private BrandName name;

    public BusBrand() {
    }

    public BusBrand(BrandName name) {
        this.name = name;
    }

    // Method to update brand name
    public void updateName(BrandName name) {
        this.name = name;
    }
}
