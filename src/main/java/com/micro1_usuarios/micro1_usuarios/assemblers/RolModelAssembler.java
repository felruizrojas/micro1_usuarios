package com.micro1_usuarios.micro1_usuarios.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.micro1_usuarios.micro1_usuarios.controller.RolController;
import com.micro1_usuarios.micro1_usuarios.model.Rol;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class RolModelAssembler implements RepresentationModelAssembler<Rol, EntityModel<Rol>> {

    @Override
    @NonNull
    public EntityModel<Rol> toModel(@NonNull Rol rol) {
        return EntityModel.of(rol,
                linkTo(methodOn(RolController.class).listarRolPorId(rol.getId())).withSelfRel(),
                linkTo(methodOn(RolController.class).listarRoles()).withRel("roles"),
                linkTo(methodOn(RolController.class).asignarPermiso(rol.getId(), 0)).withRel("asignarPermiso"),
                linkTo(methodOn(RolController.class).desactivarRol(rol.getId())).withRel("desactivar"),
                linkTo(methodOn(RolController.class).activarRol(rol.getId())).withRel("activar"));
    }
}
