package com.civa.platform.fleet.domain.model.queries;

import com.civa.platform.shared.domain.model.valueobjects.PageRequest;

public record GetAllBusBrandsPagedQuery(
        PageRequest pageRequest
) {
    public GetAllBusBrandsPagedQuery() {
        this(PageRequest.defaultPage());
    }
}