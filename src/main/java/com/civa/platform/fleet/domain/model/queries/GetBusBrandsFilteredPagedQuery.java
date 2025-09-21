package com.civa.platform.fleet.domain.model.queries;

import com.civa.platform.shared.domain.model.valueobjects.PageRequest;

public record GetBusBrandsFilteredPagedQuery(
        String nameFilter,
        PageRequest pageRequest
) {
    public GetBusBrandsFilteredPagedQuery(String nameFilter) {
        this(nameFilter, PageRequest.defaultPage());
    }
}