package com.civa.platform.fleet.domain.services;

import com.civa.platform.fleet.domain.model.agregates.Bus;
import com.civa.platform.fleet.domain.model.commands.ActivateBusCommand;
import com.civa.platform.fleet.domain.model.commands.CreateBusCommand;
import com.civa.platform.fleet.domain.model.commands.DeactivateBusCommand;
import com.civa.platform.fleet.domain.model.commands.DeleteBusCommand;
import com.civa.platform.fleet.domain.model.commands.UpdateBusCommand;

import java.util.Optional;

public interface BusCommandService {
    Optional<Bus> handle(CreateBusCommand command);
    Optional<Bus> handle(UpdateBusCommand command);
    void handle(DeleteBusCommand command);
    Optional<Bus> handle(ActivateBusCommand command);
    Optional<Bus> handle(DeactivateBusCommand command);
}
