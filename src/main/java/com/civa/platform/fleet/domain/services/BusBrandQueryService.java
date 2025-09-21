package com.civa.platform.fleet.domain.services;

import com.civa.platform.fleet.domain.model.entities.BusBrand;
import com.civa.platform.fleet.domain.model.queries.GetAllBusBrandsQuery;
import com.civa.platform.fleet.domain.model.queries.GetAllBusBrandsPagedQuery;
import com.civa.platform.fleet.domain.model.queries.GetBusBrandsFilteredPagedQuery;
import com.civa.platform.fleet.domain.model.queries.GetBusBrandDependenciesQuery;
import com.civa.platform.fleet.domain.model.valueobjects.BusBrandDependencies;
import com.civa.platform.shared.domain.model.valueobjects.PageResponse;

import java.util.List;

public interface BusBrandQueryService {
    List<BusBrand> handle(GetAllBusBrandsQuery query);
    PageResponse<BusBrand> handle(GetAllBusBrandsPagedQuery query);
    PageResponse<BusBrand> handle(GetBusBrandsFilteredPagedQuery query);
    BusBrandDependencies handle(GetBusBrandDependenciesQuery query);
}
