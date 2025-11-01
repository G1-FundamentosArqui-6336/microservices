package org.upc.iamservice.iam.domain.services;

import org.upc.iamservice.iam.domain.model.commands.SeedRolesCommand;

public interface RoleCommandService {
    void handle(SeedRolesCommand command);
}
