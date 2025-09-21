package com.civa.platform.fleet.interfaces.rest;

import com.civa.platform.fleet.domain.model.commands.DeleteBusBrandCommand;
import com.civa.platform.fleet.domain.model.commands.ForceDeleteBusBrandCommand;
import com.civa.platform.fleet.domain.model.queries.GetAllBusBrandsPagedQuery;
import com.civa.platform.fleet.domain.model.queries.GetBusBrandsFilteredPagedQuery;
import com.civa.platform.fleet.domain.model.queries.GetBusBrandDependenciesQuery;
import com.civa.platform.fleet.domain.services.BusBrandCommandService;
import com.civa.platform.fleet.domain.services.BusBrandQueryService;
import com.civa.platform.fleet.interfaces.rest.resources.BusBrandResource;
import com.civa.platform.fleet.interfaces.rest.resources.CreateBusBrandResource;
import com.civa.platform.fleet.interfaces.rest.resources.UpdateBusBrandResource;
import com.civa.platform.fleet.interfaces.rest.resources.BusBrandDependenciesResource;
import com.civa.platform.fleet.interfaces.rest.transform.BusBrandResourceFromEntityAssembler;
import com.civa.platform.fleet.interfaces.rest.transform.CreateBusBrandCommandFromResourceAssembler;
import com.civa.platform.fleet.interfaces.rest.transform.UpdateBusBrandCommandFromResourceAssembler;
import com.civa.platform.fleet.interfaces.rest.transform.BusBrandDependenciesResourceFromValueObjectAssembler;
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
@RequestMapping(value = "api/v1/bus-brands", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Bus Brands", description = "Bus Brand Management Endpoints")
public class BusBrandController {
    private final BusBrandCommandService busBrandCommandService;
    private final BusBrandQueryService busBrandQueryService;

    public BusBrandController(BusBrandCommandService busBrandCommandService, BusBrandQueryService busBrandQueryService) {
        this.busBrandCommandService = busBrandCommandService;
        this.busBrandQueryService = busBrandQueryService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Create a new bus brand",
        description = "Creates a new bus brand with the provided name. The name must be unique."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Bus brand created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BusBrandResource.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "id": 1,
                      "name": "Mercedes-Benz"
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
                      "message": "Bus brand name cannot be empty",
                      "description": "Invalid argument",
                      "timestamp": "2025-09-20T17:30:00Z"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Bus brand with the same name already exists",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 409,
                      "message": "Bus brand with same name already exists",
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
    public ResponseEntity<BusBrandResource> createBusBrand(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Bus brand creation data",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CreateBusBrandResource.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "name": "Mercedes-Benz"
                    }
                    """
                )
            )
        )
        @RequestBody CreateBusBrandResource resource) {
        var createBusBrandCommand = CreateBusBrandCommandFromResourceAssembler.toCommandFromResource(resource);
        var busBrand = busBrandCommandService.handle(createBusBrandCommand);
        var busBrandResource = BusBrandResourceFromEntityAssembler.toResourceFromEntity(busBrand.get());
        return new ResponseEntity<>(busBrandResource, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{busBrandId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Update an existing bus brand",
        description = "Updates an existing bus brand with the provided name. The name must be unique among other bus brands."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Bus brand updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BusBrandResource.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "id": 1,
                      "name": "Mercedes-Benz Updated"
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
                      "message": "Brand name is required",
                      "description": "Invalid argument"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Bus brand not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 404,
                      "message": "Bus brand not found or already deleted",
                      "description": "Resource not found"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Bus brand name already exists",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 409,
                      "message": "Bus brand with name 'Mercedes-Benz' already exists",
                      "description": "Resource already exists"
                    }
                    """
                )
            )
        )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Bus brand data to update",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UpdateBusBrandResource.class),
            examples = @ExampleObject(
                value = """
                {
                  "name": "Mercedes-Benz Updated"
                }
                """
            )
        )
    )
    public ResponseEntity<BusBrandResource> updateBusBrand(
        @Parameter(description = "ID of the bus brand to update", required = true)
        @PathVariable Long busBrandId,
        @RequestBody UpdateBusBrandResource resource) {
        var updateBusBrandCommand = UpdateBusBrandCommandFromResourceAssembler.toCommandFromResource(busBrandId, resource);
        var busBrand = busBrandCommandService.handle(updateBusBrandCommand);
        var busBrandResource = BusBrandResourceFromEntityAssembler.toResourceFromEntity(busBrand.get());
        return new ResponseEntity<>(busBrandResource, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get all bus brands with pagination",
        description = "Returns a paginated list of active bus brands with metadata. Supports sorting by name."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Bus brands retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PageResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "content": [
                        {
                          "id": 1,
                          "name": "Mercedes-Benz"
                        },
                        {
                          "id": 2,
                          "name": "Volvo"
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
    public ResponseEntity<PageResponse<BusBrandResource>> getAllBusBrands(
            @Parameter(description = "Page number (0-based)", example = "0", schema = @Schema(minimum = "0"))
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page (max 100)", example = "20", schema = @Schema(minimum = "1", maximum = "100"))
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field (name)", example = "name")
            @RequestParam(required = false) String sortBy,
            @Parameter(description = "Sort direction (asc, desc)", example = "asc", schema = @Schema(allowableValues = {"asc", "desc"}))
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        // Create PageRequest with sorting parameters - let the service layer handle the logic
        PageRequest pageRequest = createPageRequest(page, size, sortBy, sortDirection);
        
        var busBrandPage = busBrandQueryService.handle(new GetAllBusBrandsPagedQuery(pageRequest));
        var busBrandResourcePage = busBrandPage.map(BusBrandResourceFromEntityAssembler::toResourceFromEntity);
        return new ResponseEntity<>(busBrandResourcePage, HttpStatus.OK);
    }

    /**
     * Helper method to create PageRequest with sorting parameters
     */
    private PageRequest createPageRequest(int page, int size, String sortBy, String sortDirection) {
        if (sortBy != null && !sortBy.isBlank()) {
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
            case "name", "brandname" -> "name.name";
            default -> "name.name"; // default sorting by brand name
        };
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Search bus brands with pagination",
        description = "Returns a paginated list of active bus brands filtered by name using partial matching"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Bus brands search completed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PageResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "content": [
                        {
                          "id": 1,
                          "name": "Mercedes-Benz"
                        }
                      ],
                      "page": 0,
                      "size": 20,
                      "totalElements": 1,
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
            description = "Invalid search parameters",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 400,
                      "message": "Name parameter is required for search",
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
    public ResponseEntity<PageResponse<BusBrandResource>> searchBusBrandsPaged(
            @Parameter(description = "Filter by brand name (partial match)", example = "Mercedes", required = true)
            @RequestParam String name,
            @Parameter(description = "Page number (0-based)", example = "0", schema = @Schema(minimum = "0"))
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page (max 100)", example = "20", schema = @Schema(minimum = "1", maximum = "100"))
            @RequestParam(defaultValue = "20") int size) {
        
        var pageRequest = PageRequest.of(page, size);
        var busBrandPage = busBrandQueryService.handle(new GetBusBrandsFilteredPagedQuery(name, pageRequest));
        var busBrandResourcePage = busBrandPage.map(BusBrandResourceFromEntityAssembler::toResourceFromEntity);
        return new ResponseEntity<>(busBrandResourcePage, HttpStatus.OK);
    }

    @GetMapping(value = "/{busBrandId}/dependencies", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get bus brand dependencies",
        description = "Returns detailed information about dependencies (active, inactive, and total buses) for a specific bus brand. Helps determine if the brand can be safely deleted."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Dependencies information retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BusBrandDependenciesResource.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "busBrandId": 1,
                      "brandName": "Mercedes-Benz",
                      "activeBusesCount": 3,
                      "inactiveBusesCount": 1,
                      "totalBusesCount": 4,
                      "canBeDeleted": false
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Bus brand not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 404,
                      "message": "Bus brand not found or already deleted",
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

    public ResponseEntity<BusBrandDependenciesResource> getBusBrandDependencies(
        @Parameter(description = "Bus brand ID", example = "1", required = true)
        @PathVariable Long busBrandId) {
        var query = new GetBusBrandDependenciesQuery(busBrandId);
        var dependencies = busBrandQueryService.handle(query);
        var dependenciesResource = BusBrandDependenciesResourceFromValueObjectAssembler.toResourceFromValueObject(dependencies);
        return ResponseEntity.ok(dependenciesResource);
    }

    @DeleteMapping(value = "/{busBrandId}")
    @Operation(
        summary = "Delete a bus brand",
        description = "Performs logical deletion of a bus brand by setting isActive to false. The operation fails if there are active buses using this brand to maintain data integrity."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Bus brand deleted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SuccessMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 200,
                      "message": "Bus brand deleted successfully"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Bus brand not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 404,
                      "message": "Bus brand not found or already deleted",
                      "description": "Resource not found",
                      "timestamp": "2025-09-20T17:30:00Z"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Cannot delete bus brand due to dependencies",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 409,
                      "message": "Cannot delete bus brand. There are 3 active buses using this brand. Please deactivate or reassign these buses before deleting the brand.",
                      "description": "Dependency conflict detected",
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
    public ResponseEntity<SuccessMessage> deleteBusBrand(
        @Parameter(description = "Bus brand ID to delete", example = "1", required = true)
        @PathVariable Long busBrandId) {
        var deleteBusBrandCommand = new DeleteBusBrandCommand(busBrandId);
        busBrandCommandService.handle(deleteBusBrandCommand);
        var successMessage = new SuccessMessage(200, "Bus brand deleted successfully");
        return ResponseEntity.ok(successMessage);
    }

    @DeleteMapping(value = "/{busBrandId}/force")
    @Operation(
        summary = "Force delete a bus brand",
        description = "Performs logical deletion of a bus brand and all associated buses by setting isActive to false. This operation bypasses dependency validation and should be used with extreme caution as it affects all buses using this brand."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Bus brand and associated buses deleted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SuccessMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 200,
                      "message": "Bus brand and associated buses deleted successfully"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Bus brand not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 404,
                      "message": "Bus brand not found or already deleted",
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
    public ResponseEntity<SuccessMessage> forceDeleteBusBrand(
        @Parameter(description = "Bus brand ID to force delete", example = "1", required = true)
        @PathVariable Long busBrandId) {
        var forceDeleteBusBrandCommand = new ForceDeleteBusBrandCommand(busBrandId);
        busBrandCommandService.handle(forceDeleteBusBrandCommand);
        var successMessage = new SuccessMessage(200, "Bus brand and associated buses deleted successfully");
        return ResponseEntity.ok(successMessage);
    }
}
