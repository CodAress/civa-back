package com.civa.platform.iam.interfaces.rest;

import com.civa.platform.iam.domain.model.commands.DeleteUserCommand;
import com.civa.platform.iam.domain.model.queries.GetAllUsersPagedQuery;
import com.civa.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.civa.platform.iam.domain.services.UserCommandService;
import com.civa.platform.iam.domain.services.UserQueryService;
import com.civa.platform.iam.interfaces.rest.resources.UserResource;
import com.civa.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import com.civa.platform.shared.domain.model.valueobjects.PageRequest;
import com.civa.platform.shared.domain.model.valueobjects.PageResponse;
import com.civa.platform.shared.interfaces.rest.resources.SuccessMessage;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "User Management Endpoints")
public class UsersController {
   private final UserQueryService userQueryService;
   private final UserCommandService userCommandService;

    public UsersController(UserQueryService userQueryService, UserCommandService userCommandService) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
    }

    @GetMapping
    @Operation(
        summary = "Get all users with pagination",
        description = "Returns a paginated list of active users with metadata. Only accessible by administrators."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Users retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PageResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "content": [
                        {
                          "id": 1,
                          "username": "john.doe",
                          "roles": ["USER"]
                        },
                        {
                          "id": 2,
                          "username": "admin.user",
                          "roles": ["ADMIN", "USER"]
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
            responseCode = "403",
            description = "Access denied - insufficient privileges",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class)
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
    public ResponseEntity<PageResponse<UserResource>> getAllUsers(
            @Parameter(description = "Page number (0-based)", example = "0", schema = @Schema(minimum = "0"))
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page (max 100)", example = "20", schema = @Schema(minimum = "1", maximum = "100"))
            @RequestParam(defaultValue = "20") int size) {
        
        var pageRequest = PageRequest.of(page, size);
        var userPage = userQueryService.handle(new GetAllUsersPagedQuery(pageRequest));
        var userResourcePage = userPage.map(UserResourceFromEntityAssembler::toResourceFromEntity);
        return ResponseEntity.ok(userResourcePage);
    }

    @GetMapping("/{userId}")
    @Operation(
        summary = "Get user by ID",
        description = "Retrieves a specific user by their ID. Only returns active users."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResource.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "id": 1,
                      "username": "john.doe",
                      "roles": ["USER"]
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 404,
                      "message": "User not found or already deleted",
                      "description": "Resource not found",
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
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class)
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
    public ResponseEntity<UserResource> getUserById(
        @Parameter(description = "User ID", example = "1", required = true)
        @PathVariable Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    @DeleteMapping("/{userId}")
    @Operation(
        summary = "Delete a user",
        description = "Performs logical deletion of a user by setting isActive to false. The user data is preserved for historical purposes. Only accessible by administrators."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User deleted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SuccessMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 200,
                      "message": "User deleted successfully"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 404,
                      "message": "User not found or already deleted",
                      "description": "Resource not found",
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
    public ResponseEntity<SuccessMessage> deleteUser(
        @Parameter(description = "User ID to delete", example = "1", required = true)
        @PathVariable Long userId) {
        var deleteUserCommand = new DeleteUserCommand(userId);
        userCommandService.handle(deleteUserCommand);
        var successMessage = new SuccessMessage(200, "User deleted successfully");
        return ResponseEntity.ok(successMessage);
    }
}
