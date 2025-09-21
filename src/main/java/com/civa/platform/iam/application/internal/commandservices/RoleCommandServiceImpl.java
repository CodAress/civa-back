package com.civa.platform.iam.application.internal.commandservices;

import com.civa.platform.iam.domain.model.commands.DeleteRoleCommand;
import com.civa.platform.iam.domain.model.commands.SeedRolesCommand;
import com.civa.platform.iam.domain.model.entities.Role;
import com.civa.platform.iam.domain.model.valueobjects.Roles;
import com.civa.platform.iam.domain.services.RoleCommandService;
import com.civa.platform.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.civa.platform.shared.application.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class RoleCommandServiceImpl implements RoleCommandService {
    private final RoleRepository roleRepository;

    public RoleCommandServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void handle(SeedRolesCommand command) {
        Arrays.stream(Roles.values()).forEach(role -> {
            if (!roleRepository.existsByName(role)) {
                roleRepository.save(new Role(Roles.valueOf(role.name())));
            }
        });
    }

    @Override
    public void handle(DeleteRoleCommand command) {
        var role = roleRepository.findByIdAndActive(command.roleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        role.delete();
        roleRepository.save(role);
    }
}
