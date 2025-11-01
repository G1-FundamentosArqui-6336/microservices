package org.upc.iamservice.iam.domain.model.commands;

import org.upc.iamservice.iam.domain.model.entities.Role;

import java.util.List;

public record SignUpCommand(
        String email,
        String password,
        String firstName,
        String lastName,
        String phone,
        List<Role> roles) {
}
