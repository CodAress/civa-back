package com.civa.platform.fleet.application.internal.queryservices;

import com.civa.platform.fleet.domain.model.agregates.Bus;
import com.civa.platform.fleet.domain.model.queries.GetAllBusesQuery;
import com.civa.platform.fleet.domain.model.queries.GetAllBusesPagedQuery;
import com.civa.platform.fleet.domain.model.queries.GetBusByIdQuery;
import com.civa.platform.fleet.domain.services.BusQueryService;
import com.civa.platform.fleet.infrastructure.persistence.jpa.repositories.BusRepository;
import com.civa.platform.shared.application.exceptions.ResourceNotFoundException;
import com.civa.platform.shared.domain.model.valueobjects.PageResponse;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusQueryServiceImpl implements BusQueryService {

    private final BusRepository busRepository;
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BusQueryServiceImpl.class);

    public BusQueryServiceImpl(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    @Override
    public Optional<Bus> handle(GetBusByIdQuery query) {
        return busRepository.findByIdAndActive(query.id()).or(() -> {
            LOGGER.error("Active bus with id {} not found", query.id());
            return Optional.empty();
        });
    }

    @Override
    public List<Bus> handle(GetAllBusesQuery query) {
        var buses = busRepository.findAllActive();
        if(buses.isEmpty())
        {
            LOGGER.error("No active buses found");
            throw new ResourceNotFoundException("No buses found");
        }

        LOGGER.info("Found {} active buses", buses.size());
        return buses;
    }

    @Override
    public PageResponse<Bus> handle(GetAllBusesPagedQuery query) {
        var pageable = query.pageRequest().toPageable();
        var busPage = busRepository.findAllActive(pageable);
        
        LOGGER.info("Found {} active buses on page {} of {}", 
                   busPage.getNumberOfElements(), 
                   busPage.getNumber(), 
                   busPage.getTotalPages());
        
        return PageResponse.from(busPage);
    }
}
