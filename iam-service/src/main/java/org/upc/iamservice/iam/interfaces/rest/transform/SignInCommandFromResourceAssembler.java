package org.upc.iamservice.iam.interfaces.rest.transform;

import org.upc.iamservice.iam.domain.model.commands.SignInCommand;
import org.upc.iamservice.iam.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource resource) {
        return new SignInCommand(resource.email(), resource.password());
    }
}
