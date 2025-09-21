package com.civa.platform.fleet.application.internal.commandservices;

import com.civa.platform.fleet.domain.model.commands.CreateBusBrandCommand;
import com.civa.platform.fleet.domain.model.commands.DeleteBusBrandCommand;
import com.civa.platform.fleet.domain.model.commands.ForceDeleteBusBrandCommand;
import com.civa.platform.fleet.domain.model.commands.UpdateBusBrandCommand;
import com.civa.platform.fleet.domain.model.entities.BusBrand;
import com.civa.platform.fleet.domain.model.valueobjects.BrandName;
import com.civa.platform.fleet.domain.services.BusBrandCommandService;
import com.civa.platform.fleet.infrastructure.persistence.jpa.repositories.BusBrandRepository;
import com.civa.platform.fleet.infrastructure.persistence.jpa.repositories.BusRepository;
import com.civa.platform.shared.application.exceptions.DependencyConflictException;
import com.civa.platform.shared.application.exceptions.ResourceAlreadyException;
import com.civa.platform.shared.application.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BusBrandCommandServiceImpl implements BusBrandCommandService {

    private final BusBrandRepository busBrandRepository;
    private final BusRepository busRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(BusBrandCommandServiceImpl.class.getName());


    public BusBrandCommandServiceImpl(BusBrandRepository busBrandRepository, BusRepository busRepository) {
        this.busBrandRepository = busBrandRepository;
        this.busRepository = busRepository;
    }

    @Override
    public Optional<BusBrand> handle(CreateBusBrandCommand command) {
        if (busBrandRepository.existsByName(new BrandName(command.name()))) {
            LOGGER.error("Bus brand with same name already exists");
            throw new ResourceAlreadyException("Bus brand with same name already exists");
        }
        var busBrand = new BusBrand(new BrandName(command.name()));
        try {
            busBrandRepository.save(busBrand);
        } catch (Exception e) {
            LOGGER.error("Error while saving bus brand: {}", e.getMessage());
        }
        return Optional.of(busBrand);
    }

    @Override
    public Optional<BusBrand> handle(UpdateBusBrandCommand command) {
        // Verificar que la marca existe y está activa
        var existingBrand = busBrandRepository.findByIdAndActive(command.brandId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus brand not found or already deleted"));

        // Verificar que el nombre no esté en uso por otra marca
        if (!existingBrand.getName().name().equals(command.name()) && 
            busBrandRepository.existsByName(new BrandName(command.name()))) {
            throw new ResourceAlreadyException("Bus brand with name '" + command.name() + "' already exists");
        }

        // Actualizar el nombre de la marca
        existingBrand.updateName(new BrandName(command.name()));

        try {
            busBrandRepository.save(existingBrand);
            LOGGER.info("Bus brand with id {} has been updated", command.brandId());
        } catch (Exception e) {
            LOGGER.error("Error while updating bus brand: {}", e.getMessage());
            return Optional.empty();
        }
        return Optional.of(existingBrand);
    }

    @Override
    public void handle(DeleteBusBrandCommand command) {
        var busBrand = busBrandRepository.findByIdAndActive(command.busBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus brand not found or already deleted"));
        
        // Validar que no existan buses activos con esta marca
        long activeBusesCount = busRepository.countActiveBusesByBrandId(command.busBrandId());
        if (activeBusesCount > 0) {
            throw new DependencyConflictException(
                String.format("Cannot delete bus brand. There are %d active buses using this brand. " +
                            "Please delete or reassign these buses before deleting the brand.", activeBusesCount)
            );
        }
        
        busBrand.delete(); // Logical deletion
        busBrandRepository.save(busBrand);
        
        LOGGER.info("Bus brand with id {} has been logically deleted", command.busBrandId());
    }

    @Override
    public void handle(ForceDeleteBusBrandCommand command) {
        var busBrand = busBrandRepository.findByIdAndActive(command.busBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus brand not found or already deleted"));
        
        // Primero desactivar todos los buses que usen esta marca
        var activeBuses = busRepository.findActiveBusesByBrandId(command.busBrandId());
        LOGGER.info("Force deleting bus brand with id {}. Found {} active buses to deactivate", 
                   command.busBrandId(), activeBuses.size());
        
        for (var bus : activeBuses) {
            bus.delete(); // Logical deletion of each bus
            busRepository.save(bus);
        }
        
        // Luego eliminar la marca
        busBrand.delete(); // Logical deletion
        busBrandRepository.save(busBrand);
        
        LOGGER.info("Bus brand with id {} and {} associated buses have been forcefully deleted", 
                   command.busBrandId(), activeBuses.size());
    }
}
