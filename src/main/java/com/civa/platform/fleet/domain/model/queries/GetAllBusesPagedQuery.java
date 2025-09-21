package com.civa.platform.fleet.domain.model.queries;

import com.civa.platform.shared.domain.model.valueobjects.PageRequest;

public record GetAllBusesPagedQuery(
        PageRequest pageRequest
) {
    public GetAllBusesPagedQuery() {
        this(PageRequest.defaultPage());
    }
}