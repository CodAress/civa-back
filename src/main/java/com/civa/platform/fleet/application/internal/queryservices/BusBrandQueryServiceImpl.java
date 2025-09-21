package com.civa.platform.fleet.application.internal.queryservices;

import com.civa.platform.fleet.domain.model.entities.BusBrand;
import com.civa.platform.fleet.domain.model.queries.GetAllBusBrandsQuery;
import com.civa.platform.fleet.domain.model.queries.GetAllBusBrandsPagedQuery;
import com.civa.platform.fleet.domain.model.queries.GetBusBrandsFilteredPagedQuery;
import com.civa.platform.fleet.domain.model.queries.GetBusBrandDependenciesQuery;
import com.civa.platform.fleet.domain.model.valueobjects.BusBrandDependencies;
import com.civa.platform.fleet.domain.services.BusBrandQueryService;
import com.civa.platform.fleet.infrastructure.persistence.jpa.repositories.BusBrandRepository;
import com.civa.platform.fleet.infrastructure.persistence.jpa.repositories.BusRepository;
import com.civa.platform.shared.application.exceptions.ResourceNotFoundException;
import com.civa.platform.shared.domain.model.valueobjects.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusBrandQueryServiceImpl implements BusBrandQueryService {

    private final BusBrandRepository busBrandRepository;
    private final BusRepository busRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(BusBrandQueryServiceImpl.class);

    public BusBrandQueryServiceImpl(BusBrandRepository busBrandRepository, BusRepository busRepository) {
        this.busBrandRepository = busBrandRepository;
        this.busRepository = busRepository;
    }


    @Override
    public List<BusBrand> handle(GetAllBusBrandsQuery query) {

        try {
            var brands = busBrandRepository.findAllActive();
            if(brands.isEmpty()) throw new ResourceNotFoundException("No active brands found");
            LOGGER.info("Found {} active brands", brands.size());
            return brands;
        }
        catch (Exception e) {
            LOGGER.error("Error while fetching active brands: {}", e.getMessage());
            throw new RuntimeException("Error while fetching brands");
        }
    }

    @Override
    public PageResponse<BusBrand> handle(GetAllBusBrandsPagedQuery query) {
        try {
            var pageable = query.pageRequest().toPageable();
            
            // Usar el método optimizado del repository que ya devuelve Page<T> solo activos
            var brandPage = busBrandRepository.findAllActive(pageable);
            
            LOGGER.info("Found {} active brands on page {} of {} (total: {} elements)", 
                       brandPage.getNumberOfElements(), 
                       brandPage.getNumber(), 
                       brandPage.getTotalPages(),
                       brandPage.getTotalElements());
            
            return PageResponse.from(brandPage);
        }
        catch (Exception e) {
            LOGGER.error("Error while fetching paged active brands: {}", e.getMessage());
            throw new RuntimeException("Error while fetching paged brands");
        }
    }

    @Override
    public PageResponse<BusBrand> handle(GetBusBrandsFilteredPagedQuery query) {
        try {
            var pageable = query.pageRequest().toPageable();
            
            // Usar filtro eficiente directamente en la BD (solo activos)
            var brandPage = busBrandRepository.findByNameContainingAndActive(query.nameFilter(), pageable);
            
            LOGGER.info("Found {} filtered active brands (filter: '{}') on page {} of {} (total: {} elements)", 
                       brandPage.getNumberOfElements(),
                       query.nameFilter(),
                       brandPage.getNumber(), 
                       brandPage.getTotalPages(),
                       brandPage.getTotalElements());
            
            return PageResponse.from(brandPage);
        }
        catch (Exception e) {
            LOGGER.error("Error while fetching filtered paged active brands: {}", e.getMessage());
            throw new RuntimeException("Error while fetching filtered paged brands");
        }
    }

    @Override
    public BusBrandDependencies handle(GetBusBrandDependenciesQuery query) {
        var busBrand = busBrandRepository.findByIdAndActive(query.busBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus brand not found or already deleted"));
        
        Long activeBusesCount = busRepository.countActiveBusesByBrandId(query.busBrandId());
        Long inactiveBusesCount = busRepository.countInactiveBusesByBrandId(query.busBrandId());
        Long totalBusesCount = busRepository.countTotalBusesByBrandId(query.busBrandId());
        boolean canBeDeleted = activeBusesCount == 0;
        
        return new BusBrandDependencies(
                query.busBrandId(),
                busBrand.getName().name(),
                activeBusesCount,
                inactiveBusesCount,
                totalBusesCount,
                canBeDeleted
        );
    }
}
