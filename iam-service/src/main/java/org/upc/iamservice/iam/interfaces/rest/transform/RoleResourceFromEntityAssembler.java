package org.upc.iamservice.iam.interfaces.rest.transform;

import org.upc.iamservice.iam.domain.model.entities.Role;
import org.upc.iamservice.iam.interfaces.rest.resources.RoleResource;

public class RoleResourceFromEntityAssembler {
    public static RoleResource toResourceFromEntity(Role entity) {
        return new RoleResource(entity.getId(), entity.getStringName());
    }
}
