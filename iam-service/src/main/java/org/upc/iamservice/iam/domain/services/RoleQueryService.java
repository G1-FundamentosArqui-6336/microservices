package org.upc.iamservice.iam.domain.services;

import org.upc.iamservice.iam.domain.model.entities.Role;
import org.upc.iamservice.iam.domain.model.queries.GetAllRolesQuery;
import org.upc.iamservice.iam.domain.model.queries.GetRoleByIdQuery;

import java.util.List;
import java.util.Optional;

public interface RoleQueryService {
    List<Role> handle(GetAllRolesQuery query);
    Optional<Role> handle(GetRoleByIdQuery query);
}
