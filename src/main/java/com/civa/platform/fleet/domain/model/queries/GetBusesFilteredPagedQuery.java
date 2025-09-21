package com.civa.platform.fleet.domain.model.queries;

import com.civa.platform.shared.domain.model.valueobjects.PageRequest;

public record GetBusesFilteredPagedQuery(
        String busNumberFilter,
        String licensePlateFilter,
        PageRequest pageRequest
) {
    public GetBusesFilteredPagedQuery(String busNumberFilter, String licensePlateFilter) {
        this(busNumberFilter, licensePlateFilter, PageRequest.defaultPage());
    }
}