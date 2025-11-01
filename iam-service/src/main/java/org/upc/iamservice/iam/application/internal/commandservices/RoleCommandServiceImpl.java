package org.upc.iamservice.iam.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.upc.iamservice.iam.domain.model.commands.SeedRolesCommand;
import org.upc.iamservice.iam.domain.model.entities.Role;
import org.upc.iamservice.iam.domain.model.valueobjects.Roles;
import org.upc.iamservice.iam.domain.services.RoleCommandService;
import org.upc.iamservice.iam.infrastructure.persistence.jpa.repositories.RoleRepository;

import java.util.Arrays;

@Service
public class RoleCommandServiceImpl implements RoleCommandService {
    private final RoleRepository roleRepository;

    public RoleCommandServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void handle(SeedRolesCommand command) {
        Arrays.stream(Roles.values()).forEach(role -> {
            if (!roleRepository.existsByName(role)) {
                roleRepository.save(new Role(Roles.valueOf(role.name())));
            }
        });
    }
}
