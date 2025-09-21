package com.civa.platform.fleet.interfaces.rest;

import com.civa.platform.fleet.domain.model.commands.ActivateBusCommand;
import com.civa.platform.fleet.domain.model.commands.DeactivateBusCommand;
import com.civa.platform.fleet.domain.model.commands.DeleteBusCommand;
import com.civa.platform.fleet.domain.model.queries.GetAllBusesPagedQuery;
import com.civa.platform.fleet.domain.model.queries.GetBusByIdQuery;
import com.civa.platform.fleet.domain.services.BusCommandService;
import com.civa.platform.fleet.domain.services.BusQueryService;
import com.civa.platform.fleet.interfaces.rest.resources.BusResource;
import com.civa.platform.fleet.interfaces.rest.resources.CreateBusResource;
import com.civa.platform.fleet.interfaces.rest.resources.UpdateBusResource;
import com.civa.platform.fleet.interfaces.rest.transform.BusResourceFromEntityAssembler;
import com.civa.platform.fleet.interfaces.rest.transform.CreateBusCommandFromResourceAssembler;
import com.civa.platform.fleet.interfaces.rest.transform.UpdateBusCommandFromResourceAssembler;
import com.civa.platform.shared.domain.model.valueobjects.PageRequest;
import com.civa.platform.shared.domain.model.valueobjects.PageResponse;
import com.civa.platform.shared.domain.model.valueobjects.SortOrder;
import com.civa.platform.shared.interfaces.rest.resources.SuccessMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/buses", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Bus", description = "Bus Management Endpoints")
public class BusController {
    private final BusCommandService busCommandService;
    private final BusQueryService busQueryService;

    public BusController(BusCommandService busCommandService, BusQueryService busQueryService) {
        this.busCommandService = busCommandService;
        this.busQueryService = busQueryService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Create a new bus",
        description = "Creates a new bus with the provided details. 'number', 'licensePlate', 'brandId', and 'features' are required. 'status' is optional and defaults to ACTIVE if not provided."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Bus created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BusResource.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "id": 1,
                      "busNumber": "B001",
                      "licensePlate": "ABC-123",
                      "busBrandId": 1,
                      "busBrandName": "Mercedes-Benz",
                      "status": "ACTIVE"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 400,
                      "message": "Bus number cannot be empty",
                      "description": "Invalid argument",
                      "timestamp": "2025-09-20T17:30:00Z"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Bus with same number or license plate already exists",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 409,
                      "message": "Bus with same license plate already exists",
                      "description": "Resource already exists",
                      "timestamp": "2025-09-20T17:30:00Z"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class)
            )
        )
    })
    public ResponseEntity<BusResource> createBus(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Bus creation data. Fields 'number', 'licensePlate', 'brandId', and 'features' are required. Field 'status' is optional (defaults to ACTIVE).",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CreateBusResource.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "number": "B001",
                      "licensePlate": "ABC-123",
                      "brandId": 1,
                      "features": "Air conditioning, WiFi, USB ports"
                    }
                    """
                )
            )
        )
        @RequestBody CreateBusResource resource) {
        var createBusCommand = CreateBusCommandFromResourceAssembler.toCommandFromResource(resource);
        var bus = busCommandService.handle(createBusCommand);
        var busResource = BusResourceFromEntityAssembler.toResourceFromEntity(bus.get());
        return new ResponseEntity<>(busResource, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{busId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Update an existing bus",
        description = "Updates an existing bus with the provided details. All fields except 'status' are required. If 'status' is not provided, the current status is maintained."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Bus updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BusResource.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "id": 1,
                      "busNumber": "B001-UPDATED",
                      "licensePlate": "XYZ-789",
                      "busBrandId": 2,
                      "busBrandName": "Volvo",
                      "features": "Air conditioning, WiFi, USB ports, Reclining seats",
                      "status": "ACTIVE"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 400,
                      "message": "Bus number cannot be empty",
                      "description": "Invalid argument"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Bus not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 404,
                      "message": "Bus not found or already deleted",
                      "description": "Resource not found"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Bus with number or license plate already exists",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 409,
                      "message": "Bus with number B001 already exists",
                      "description": "Resource already exists"
                    }
                    """
                )
            )
        )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Bus data to update",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UpdateBusResource.class),
            examples = @ExampleObject(
                value = """
                {
                  "number": "B001-UPDATED",
                  "licensePlate": "XYZ-789",
                  "brandId": 2,
                  "features": "Air conditioning, WiFi, USB ports, Reclining seats",
                  "status": "ACTIVE"
                }
                """
            )
        )
    )
    public ResponseEntity<BusResource> updateBus(
        @Parameter(description = "ID of the bus to update", required = true)
        @PathVariable Long busId,
        @RequestBody UpdateBusResource resource) {
        var updateBusCommand = UpdateBusCommandFromResourceAssembler.toCommandFromResource(busId, resource);
        var bus = busCommandService.handle(updateBusCommand);
        var busResource = BusResourceFromEntityAssembler.toResourceFromEntity(bus.get());
        return new ResponseEntity<>(busResource, HttpStatus.OK);
    }

    @GetMapping(value = "/{busId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get bus by ID",
        description = "Retrieves a specific bus by its ID. Only returns active buses."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Bus retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BusResource.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "id": 1,
                      "busNumber": "B001",
                      "licensePlate": "ABC-123",
                      "busBrandId": 1,
                      "busBrandName": "Mercedes-Benz",
                      "status": "ACTIVE"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Bus not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 404,
                      "message": "Bus not found or already deleted",
                      "description": "Resource not found",
                      "timestamp": "2025-09-20T17:30:00Z"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class)
            )
        )
    })
    public ResponseEntity<BusResource> getBusById(
        @Parameter(description = "Bus ID", example = "1", required = true)
        @PathVariable Long busId) {
        var getBusByIdQuery = new GetBusByIdQuery(busId);
        var bus = busQueryService.handle(getBusByIdQuery);
        var busResource = BusResourceFromEntityAssembler.toResourceFromEntity(bus.get());
        return new ResponseEntity<>(busResource, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get all buses with pagination",
        description = "Returns a paginated list of active buses with metadata. Supports sorting by various fields like busNumber, licensePlate, etc."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Buses retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PageResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "content": [
                        {
                          "id": 1,
                          "busNumber": "B001",
                          "licensePlate": "ABC-123",
                          "busBrandId": 1,
                          "busBrandName": "Mercedes-Benz",
                          "status": "ACTIVE"
                        },
                        {
                          "id": 2,
                          "busNumber": "B002",
                          "licensePlate": "DEF-456",
                          "busBrandId": 2,
                          "busBrandName": "Volvo",
                          "status": "INACTIVE"
                        }
                      ],
                      "page": 0,
                      "size": 20,
                      "totalElements": 2,
                      "totalPages": 1,
                      "first": true,
                      "last": true
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid pagination parameters",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 400,
                      "message": "Page size must be between 1 and 100",
                      "description": "Invalid argument",
                      "timestamp": "2025-09-20T17:30:00Z"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class)
            )
        )
    })
    public ResponseEntity<PageResponse<BusResource>> getAllBuses(
            @Parameter(description = "Page number (0-based)", example = "0", schema = @Schema(minimum = "0"))
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page (max 100)", example = "20", schema = @Schema(minimum = "1", maximum = "100"))
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field (busNumber, licensePlate, features, status, brand)", example = "busNumber")
            @RequestParam(required = false) String sortBy,
            @Parameter(description = "Sort direction (asc/desc)", example = "asc", schema = @Schema(allowableValues = {"asc", "desc"}))
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        // Create PageRequest with sorting parameters - let the service layer handle the logic
        PageRequest pageRequest = createPageRequest(page, size, sortBy, sortDirection);
        
        var busPage = busQueryService.handle(new GetAllBusesPagedQuery(pageRequest));
        var busResourcePage = busPage.map(BusResourceFromEntityAssembler::toResourceFromEntity);
        return new ResponseEntity<>(busResourcePage, HttpStatus.OK);
    }

    /**
     * Helper method to create PageRequest with sorting parameters
     */
    private PageRequest createPageRequest(int page, int size, String sortBy, String sortDirection) {
        if (sortBy != null && !sortBy.isEmpty()) {
            // Map API field names to JPA entity field names
            String mappedSortBy = mapSortFieldToEntityField(sortBy);
            
            var direction = "desc".equalsIgnoreCase(sortDirection) 
                ? SortOrder.Direction.DESC 
                : SortOrder.Direction.ASC;
            var sortOrder = new SortOrder(mappedSortBy, direction);
            return PageRequest.of(page, size, sortOrder);
        } else {
            return PageRequest.of(page, size);
        }
    }

    /**
     * Maps API sort field names to actual JPA entity field names
     */
    private String mapSortFieldToEntityField(String apiFieldName) {
        return switch (apiFieldName.toLowerCase()) {
            case "busnumber", "number" -> "number.number";
            case "licenseplate", "plate" -> "licensePlate.plate";
            case "features" -> "features.features";
            case "status" -> "status";
            case "brand", "brandname" -> "busBrand.name.name";
            default -> "number.number"; // default sorting by bus number
        };
    }

    @DeleteMapping(value = "/{busId}")
    @Operation(
        summary = "Delete a bus",
        description = "Performs logical deletion of a bus by setting isActive to false. The bus will no longer appear in active searches but data is preserved for historical purposes."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Bus deleted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SuccessMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 200,
                      "message": "Bus deleted successfully"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Bus not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 404,
                      "message": "Bus not found or already deleted",
                      "description": "Resource not found",
                      "timestamp": "2025-09-20T17:30:00Z"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class)
            )
        )
    })
    public ResponseEntity<SuccessMessage> deleteBus(
        @Parameter(description = "Bus ID to delete", example = "1", required = true)
        @PathVariable Long busId) {
        var deleteBusCommand = new DeleteBusCommand(busId);
        busCommandService.handle(deleteBusCommand);
        var successMessage = new SuccessMessage(200, "Bus deleted successfully");
        return ResponseEntity.ok(successMessage);
    }

    @PatchMapping(value = "/{busId}/activate")
    @Operation(
        summary = "Activate a bus",
        description = "Changes the bus status to ACTIVE, making it available for operations. Also reactivates the bus if it was logically deleted."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Bus activated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BusResource.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "id": 1,
                      "busNumber": "B001",
                      "licensePlate": "ABC-123",
                      "busBrandId": 1,
                      "busBrandName": "Mercedes-Benz",
                      "status": "ACTIVE"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Bus not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 404,
                      "message": "Bus not found",
                      "description": "Resource not found",
                      "timestamp": "2025-09-20T17:30:00Z"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class)
            )
        )
    })
    public ResponseEntity<BusResource> activateBus(
        @Parameter(description = "Bus ID to activate", example = "1", required = true)
        @PathVariable Long busId) {
        var activateBusCommand = new ActivateBusCommand(busId);
        var bus = busCommandService.handle(activateBusCommand);
        var busResource = BusResourceFromEntityAssembler.toResourceFromEntity(bus.get());
        return ResponseEntity.ok(busResource);
    }

    @PatchMapping(value = "/{busId}/deactivate")
    @Operation(
        summary = "Deactivate a bus",
        description = "Changes the bus status to INACTIVE, making it unavailable for operations. The bus remains in the system but cannot be used for routes or services."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Bus deactivated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BusResource.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "id": 1,
                      "busNumber": "B001",
                      "licensePlate": "ABC-123",
                      "busBrandId": 1,
                      "busBrandName": "Mercedes-Benz",
                      "status": "INACTIVE"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Bus not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 404,
                      "message": "Bus not found",
                      "description": "Resource not found",
                      "timestamp": "2025-09-20T17:30:00Z"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class)
            )
        )
    })
    public ResponseEntity<BusResource> deactivateBus(
        @Parameter(description = "Bus ID to deactivate", example = "1", required = true)
        @PathVariable Long busId) {
        var deactivateBusCommand = new DeactivateBusCommand(busId);
        var bus = busCommandService.handle(deactivateBusCommand);
        var busResource = BusResourceFromEntityAssembler.toResourceFromEntity(bus.get());
        return ResponseEntity.ok(busResource);
    }
}
