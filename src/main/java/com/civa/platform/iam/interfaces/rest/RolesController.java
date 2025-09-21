package com.civa.platform.iam.interfaces.rest;

import com.civa.platform.iam.domain.model.queries.GetAllRolesPagedQuery;
import com.civa.platform.iam.domain.services.RoleCommandService;
import com.civa.platform.iam.domain.services.RoleQueryService;
import com.civa.platform.iam.interfaces.rest.resources.RoleResource;
import com.civa.platform.iam.interfaces.rest.transform.RoleResourceFromEntityAssembler;
import com.civa.platform.shared.domain.model.valueobjects.PageRequest;
import com.civa.platform.shared.domain.model.valueobjects.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/roles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Roles", description = "Role Management Endpoints")
public class RolesController {
    private final RoleQueryService roleQueryService;
    private final RoleCommandService roleCommandService;

    public RolesController(RoleQueryService roleQueryService, RoleCommandService roleCommandService) {
        this.roleQueryService = roleQueryService;
        this.roleCommandService = roleCommandService;
    }

    @GetMapping
    @Operation(
        summary = "Get all roles with pagination",
        description = "Returns a paginated list of system roles with metadata. Used for role management and assignment. Only accessible by administrators."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Roles retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PageResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "content": [
                        {
                          "id": 1,
                          "name": "ADMIN"
                        },
                        {
                          "id": 2,
                          "name": "USER"
                        },
                        {
                          "id": 3,
                          "name": "OPERATOR"
                        }
                      ],
                      "page": 0,
                      "size": 20,
                      "totalElements": 3,
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
            responseCode = "403",
            description = "Access denied - insufficient privileges",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 403,
                      "message": "Access denied - admin privileges required",
                      "description": "Forbidden",
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
    public ResponseEntity<PageResponse<RoleResource>> getAllRoles(
            @Parameter(description = "Page number (0-based)", example = "0", schema = @Schema(minimum = "0"))
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page (max 100)", example = "20", schema = @Schema(minimum = "1", maximum = "100"))
            @RequestParam(defaultValue = "20") int size) {
        
        var pageRequest = PageRequest.of(page, size);
        var rolePage = roleQueryService.handle(new GetAllRolesPagedQuery(pageRequest));
        var roleResourcePage = rolePage.map(RoleResourceFromEntityAssembler::toResourceFromEntity);
        return ResponseEntity.ok(roleResourcePage);
    }

    /*
     
     
    @DeleteMapping("/{roleId}")
    @Operation(
        summary = "Delete a role",
        description = "Performs logical deletion of a role by setting isActive to false"
    )
    public ResponseEntity<SuccessMessage> deleteRole(@PathVariable Long roleId) {
        var deleteRoleCommand = new DeleteRoleCommand(roleId);
        roleCommandService.handle(deleteRoleCommand);
        var successMessage = new SuccessMessage(200, "Role deleted successfully");
        return ResponseEntity.ok(successMessage);
    }
    */
}
