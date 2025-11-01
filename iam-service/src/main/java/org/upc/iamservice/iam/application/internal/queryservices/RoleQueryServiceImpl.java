package org.upc.iamservice.iam.application.internal.queryservices;

import org.springframework.stereotype.Service;
import org.upc.iamservice.iam.domain.model.entities.Role;
import org.upc.iamservice.iam.domain.model.queries.GetAllRolesQuery;
import org.upc.iamservice.iam.domain.model.queries.GetRoleByIdQuery;
import org.upc.iamservice.iam.domain.services.RoleQueryService;
import org.upc.iamservice.iam.infrastructure.persistence.jpa.repositories.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoleQueryServiceImpl implements RoleQueryService {
    private final RoleRepository roleRepository;

    public RoleQueryServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> handle(GetAllRolesQuery query) {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> handle(GetRoleByIdQuery query) {
        return roleRepository.findById(query.roleId());
    }
}
