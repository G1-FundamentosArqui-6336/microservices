package org.upc.iamservice.iam.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.upc.iamservice.iam.domain.model.queries.GetAllRolesQuery;
import org.upc.iamservice.iam.domain.services.RoleQueryService;
import org.upc.iamservice.iam.interfaces.rest.resources.RoleResource;
import org.upc.iamservice.iam.interfaces.rest.transform.RoleResourceFromEntityAssembler;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/roles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Roles", description = "Role Management Endpoints")
public class RolesController {

    private final RoleQueryService roleQueryService;

    public RolesController(RoleQueryService roleQueryService) {
        this.roleQueryService = roleQueryService;
    }

    @Operation(summary = "Get all roles",
            description = "Retrieves a list of all available user roles in the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of roles retrieved successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = RoleResource.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    @GetMapping
    public ResponseEntity<List<RoleResource>> getAllRoles() {
        var getAllRolesQuery = new GetAllRolesQuery();
        var roles = roleQueryService.handle(getAllRolesQuery);
        var roleResources = roles.stream()
                .map(RoleResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(roleResources);
    }
}
