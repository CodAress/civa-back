package com.civa.platform.iam.domain.model.queries;

import com.civa.platform.shared.domain.model.valueobjects.PageRequest;

public record GetAllRolesPagedQuery(
        PageRequest pageRequest
) {
    public GetAllRolesPagedQuery() {
        this(PageRequest.defaultPage());
    }
}