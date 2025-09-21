package com.civa.platform.iam.domain.model.queries;

import com.civa.platform.shared.domain.model.valueobjects.PageRequest;

public record GetAllUsersPagedQuery(
        PageRequest pageRequest
) {
    public GetAllUsersPagedQuery() {
        this(PageRequest.defaultPage());
    }
}