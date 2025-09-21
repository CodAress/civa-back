package com.civa.platform.iam.domain.services;

import com.civa.platform.iam.domain.model.commands.DeleteRoleCommand;
import com.civa.platform.iam.domain.model.commands.SeedRolesCommand;

public interface RoleCommandService {
    void handle(SeedRolesCommand command);
    void handle(DeleteRoleCommand command);
}