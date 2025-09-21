package com.civa.platform.fleet.domain.services;

import com.civa.platform.fleet.domain.model.commands.CreateBusBrandCommand;
import com.civa.platform.fleet.domain.model.commands.DeleteBusBrandCommand;
import com.civa.platform.fleet.domain.model.commands.ForceDeleteBusBrandCommand;
import com.civa.platform.fleet.domain.model.commands.UpdateBusBrandCommand;
import com.civa.platform.fleet.domain.model.entities.BusBrand;

import java.util.Optional;

public interface BusBrandCommandService {
    Optional<BusBrand> handle(CreateBusBrandCommand command);
    Optional<BusBrand> handle(UpdateBusBrandCommand command);
    void handle(DeleteBusBrandCommand command);
    void handle(ForceDeleteBusBrandCommand command);
}
