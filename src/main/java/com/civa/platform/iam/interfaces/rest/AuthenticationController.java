package com.civa.platform.iam.interfaces.rest;

import com.civa.platform.iam.domain.services.UserCommandService;
import com.civa.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.civa.platform.iam.interfaces.rest.resources.SignInResource;
import com.civa.platform.iam.interfaces.rest.resources.SignUpResource;
import com.civa.platform.iam.interfaces.rest.resources.UserResource;
import com.civa.platform.iam.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import com.civa.platform.iam.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import com.civa.platform.iam.interfaces.rest.transform.SignUpCommandFromResourceAssembler;
import com.civa.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication Endpoints")
public class AuthenticationController {
    private final UserCommandService userCommandService;

    public AuthenticationController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @PostMapping("/sign-up")
    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account with username and password. The user will be assigned default roles."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "User registered successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResource.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "id": 1,
                      "username": "john.doe",
                      "roles": ["ROLE_USER"]
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Registration failed due to invalid data or existing username",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 400,
                      "message": "Username already exists",
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
    public ResponseEntity<UserResource> signUp(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User registration data",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SignUpResource.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "username": "john.doe",
                      "password": "SecurePassword123!",
                      "roles": ["ROLE_USER"]
                    }
                    """
                )
            )
        )
        @RequestBody SignUpResource resource) {
        var signUpCommand = SignUpCommandFromResourceAssembler.toCommandFromResource(resource);
        var user = userCommandService.handle(signUpCommand);
        if (user.isEmpty()) return ResponseEntity.badRequest().build();
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return new ResponseEntity<>(userResource, HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    @Operation(
        summary = "Authenticate user",
        description = "Authenticates a user with username and password, returning user information and access token."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Authentication successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthenticatedUserResource.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "id": 1,
                      "username": "john.doe",
                      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                      "roles": ["ROLE_USER"]
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication failed - invalid credentials",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.civa.platform.shared.interfaces.rest.resources.ErrorMessage.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "statusCode": 401,
                      "message": "Invalid username or password",
                      "description": "Authentication failed",
                      "timestamp": "2025-09-20T17:30:00Z"
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
                      "message": "User not found",
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
    public ResponseEntity<AuthenticatedUserResource> signIn(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User authentication credentials",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SignInResource.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "username": "john.doe",
                      "password": "SecurePassword123!"
                    }
                    """
                )
            )
        )
        @RequestBody SignInResource resource) {
        var signInCommand = SignInCommandFromResourceAssembler.toCommandFromResource(resource);
        var authenticatedUser = userCommandService.handle(signInCommand);
        if (authenticatedUser.isEmpty()) return ResponseEntity.notFound().build();
        var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(authenticatedUser.get().getLeft(), authenticatedUser.get().getRight());
        return ResponseEntity.ok(authenticatedUserResource);
    }
}
