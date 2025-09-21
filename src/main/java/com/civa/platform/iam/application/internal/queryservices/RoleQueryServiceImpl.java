package com.civa.platform.iam.application.internal.queryservices;

import com.civa.platform.iam.domain.model.entities.Role;
import com.civa.platform.iam.domain.model.queries.GetAllRolesQuery;
import com.civa.platform.iam.domain.model.queries.GetAllRolesPagedQuery;
import com.civa.platform.iam.domain.model.queries.GetRoleByNameQuery;
import com.civa.platform.iam.domain.services.RoleQueryService;
import com.civa.platform.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.civa.platform.shared.domain.model.valueobjects.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleQueryServiceImpl implements RoleQueryService {
    private final RoleRepository roleRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleQueryServiceImpl.class);

    public RoleQueryServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> handle(GetAllRolesQuery query) {
        var roles = roleRepository.findAllActive();
        LOGGER.info("Found {} active roles", roles.size());
        return roles;
    }

    @Override
    public PageResponse<Role> handle(GetAllRolesPagedQuery query) {
        var pageable = query.pageRequest().toPageable();
        var rolePage = roleRepository.findAllActive(pageable);
        
        LOGGER.info("Found {} active roles on page {} of {}", 
                   rolePage.getNumberOfElements(), 
                   rolePage.getNumber(), 
                   rolePage.getTotalPages());
        
        return PageResponse.from(rolePage);
    }

    @Override
    public Optional<Role> handle(GetRoleByNameQuery query) {
        return roleRepository.findByNameAndActive(query.name());
    }
}
