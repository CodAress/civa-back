package com.civa.platform.iam.interfaces.rest.transform;

import com.civa.platform.iam.domain.model.aggregates.User;
import com.civa.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;

import java.util.List;

public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User entity, String token) {
        List<String> roleNames = entity.getRoles().stream()
                .map(role -> role.getStringName())
                .toList();
        
        return new AuthenticatedUserResource(
                entity.getId(), 
                entity.getUsername(), 
                token, 
                roleNames
        );
    }
}
