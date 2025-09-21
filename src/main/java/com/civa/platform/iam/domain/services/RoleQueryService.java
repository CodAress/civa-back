package com.civa.platform.iam.domain.services;

import com.civa.platform.iam.domain.model.entities.Role;
import com.civa.platform.iam.domain.model.queries.GetAllRolesQuery;
import com.civa.platform.iam.domain.model.queries.GetAllRolesPagedQuery;
import com.civa.platform.iam.domain.model.queries.GetRoleByNameQuery;
import com.civa.platform.shared.domain.model.valueobjects.PageResponse;

import java.util.List;
import java.util.Optional;

public interface RoleQueryService {
    List<Role> handle(GetAllRolesQuery query);
    PageResponse<Role> handle(GetAllRolesPagedQuery query);
    Optional<Role> handle(GetRoleByNameQuery query);
}
