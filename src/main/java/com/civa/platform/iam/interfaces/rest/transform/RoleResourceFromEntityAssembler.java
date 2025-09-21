package com.civa.platform.iam.interfaces.rest.transform;

import com.civa.platform.iam.domain.model.entities.Role;
import com.civa.platform.iam.interfaces.rest.resources.RoleResource;

public class RoleResourceFromEntityAssembler {
    public static RoleResource toResourceFromEntity(Role entity) {
        return new RoleResource(entity.getId(), entity.getStringName());

    }
}
