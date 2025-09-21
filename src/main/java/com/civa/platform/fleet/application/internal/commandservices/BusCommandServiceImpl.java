package com.civa.platform.fleet.application.internal.commandservices;

import com.civa.platform.fleet.domain.model.agregates.Bus;
import com.civa.platform.fleet.domain.model.commands.ActivateBusCommand;
import com.civa.platform.fleet.domain.model.commands.CreateBusCommand;
import com.civa.platform.fleet.domain.model.commands.DeactivateBusCommand;
import com.civa.platform.fleet.domain.model.commands.DeleteBusCommand;
import com.civa.platform.fleet.domain.model.commands.UpdateBusCommand;
import com.civa.platform.fleet.domain.model.valueobjects.BusFeatures;
import com.civa.platform.fleet.domain.model.valueobjects.BusNumber;
import com.civa.platform.fleet.domain.model.valueobjects.BusStatus;
import com.civa.platform.fleet.domain.model.valueobjects.LicensePlate;
import com.civa.platform.fleet.domain.services.BusCommandService;
import com.civa.platform.fleet.infrastructure.persistence.jpa.repositories.BusBrandRepository;
import com.civa.platform.fleet.infrastructure.persistence.jpa.repositories.BusRepository;
import com.civa.platform.shared.application.exceptions.InvalidValueException;
import com.civa.platform.shared.application.exceptions.ResourceAlreadyException;
import com.civa.platform.shared.application.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BusCommandServiceImpl implements BusCommandService {

    private final BusRepository busRepository;
    private final BusBrandRepository busBrandRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(BusCommandServiceImpl.class);

    public BusCommandServiceImpl(BusRepository busRepository, BusBrandRepository busBrandRepository) {
        this.busRepository = busRepository;
        this.busBrandRepository = busBrandRepository;
    }

    @Override
    public Optional<Bus> handle(CreateBusCommand command) {
        if(busRepository.existsByNumber(new BusNumber(command.number()))) {
            throw new ResourceAlreadyException("Bus with number " + command.number() + " already exists");
        }
        if(busRepository.existsByLicensePlate(new LicensePlate(command.licensePlate()))) {
            throw new ResourceAlreadyException("Bus with license plate " + command.licensePlate() + " already exists");
        }

        if(!BusStatus.isValidEnum(command.getStatusOrDefault()))
        {
            throw new InvalidValueException("Invalid bus status");
        }

        var busBrand = busBrandRepository.findById(command.brandId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus brand not found"));

        var busStatus = BusStatus.fromString(command.getStatusOrDefault());
        var bus = new Bus(new BusNumber(command.number()), new LicensePlate(command.licensePlate()), busBrand, new BusFeatures(command.features()), busStatus);

        try {
            busRepository.save(bus);
        } catch (Exception e) {
            LOGGER.error("Error while saving bus: {}", e.getMessage());
            return Optional.empty();
        }
        return Optional.of(bus);
    }

    @Override
    public Optional<Bus> handle(UpdateBusCommand command) {
        // Verificar que el bus existe y está activo
        var existingBus = busRepository.findByIdAndActive(command.busId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found or already deleted"));

        // Verificar que el número no esté en uso por otro bus
        if (!existingBus.getNumber().number().equals(command.number()) && 
            busRepository.existsByNumber(new BusNumber(command.number()))) {
            throw new ResourceAlreadyException("Bus with number " + command.number() + " already exists");
        }

        // Verificar que la placa no esté en uso por otro bus
        if (!existingBus.getLicensePlate().plate().equals(command.licensePlate()) && 
            busRepository.existsByLicensePlate(new LicensePlate(command.licensePlate()))) {
            throw new ResourceAlreadyException("Bus with license plate " + command.licensePlate() + " already exists");
        }

        // Verificar que la marca existe
        var busBrand = busBrandRepository.findById(command.brandId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus brand not found"));

        // Actualizar datos del bus
        existingBus.updateDetails(
                new BusNumber(command.number()),
                new LicensePlate(command.licensePlate()),
                busBrand,
                new BusFeatures(command.features())
        );

        // Verificar y actualizar status si fue proporcionado
        if (command.status() != null && !command.status().isBlank()) {
            if (!BusStatus.isValidEnum(command.status())) {
                throw new InvalidValueException("Invalid bus status");
            }
            existingBus.updateStatus(BusStatus.fromString(command.status()));
        }

        try {
            busRepository.save(existingBus);
            LOGGER.info("Bus with id {} has been updated", command.busId());
        } catch (Exception e) {
            LOGGER.error("Error while updating bus: {}", e.getMessage());
            return Optional.empty();
        }
        return Optional.of(existingBus);
    }

    @Override
    public void handle(DeleteBusCommand command) {
        var bus = busRepository.findByIdAndActive(command.busId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));
        
        bus.delete(); // Logical deletion
        busRepository.save(bus);
        
        LOGGER.info("Bus with id {} has been logically deleted", command.busId());
    }

    @Override
    public Optional<Bus> handle(ActivateBusCommand command) {
        var bus = busRepository.findByIdAndActive(command.busId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found or already deleted"));
        
        bus.activate();
        busRepository.save(bus);
        
        LOGGER.info("Bus with id {} has been activated", command.busId());
        return Optional.of(bus);
    }

    @Override
    public Optional<Bus> handle(DeactivateBusCommand command) {
        var bus = busRepository.findByIdAndActive(command.busId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found or already deleted"));
        
        bus.deactivate();
        busRepository.save(bus);
        
        LOGGER.info("Bus with id {} has been deactivated", command.busId());
        return Optional.of(bus);
    }
}
