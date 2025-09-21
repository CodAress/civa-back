package com.civa.platform.iam.domain.model.entities;

import com.civa.platform.iam.domain.model.valueobjects.Roles;
import com.civa.platform.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
public class Role extends AuditableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, length = 20)
    private Roles name;

    public Role() {}

    public Role(Roles name) {
        this.name = name;
    }

    public String getStringName() { return name.name(); }

    public static Role getDefaultRole() { return new Role(Roles.ROLE_USER); }

    public static Role toRoleFromName(String name) {
        return new Role(Roles.valueOf(name));
    }

    public static List<Role> validateRoleSet(List<Role> roles) {
        if (roles == null || roles.isEmpty()) return List.of(getDefaultRole());
        return roles;
    }
}
