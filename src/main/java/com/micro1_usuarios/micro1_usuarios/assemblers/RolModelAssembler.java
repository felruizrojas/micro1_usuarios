package com.micro1_usuarios.micro1_usuarios.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.micro1_usuarios.micro1_usuarios.controller.RolControllerV2;
import com.micro1_usuarios.micro1_usuarios.model.Rol;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

@Component
public class RolModelAssembler implements RepresentationModelAssembler<Rol, EntityModel<Rol>> {

    @Override
    @NonNull
    public EntityModel<Rol> toModel(@NonNull Rol rol) {
        return EntityModel.of(rol,
                linkTo(methodOn(RolControllerV2.class).obtenerRolPorId(rol.getId())).withSelfRel(),
                linkTo(methodOn(RolControllerV2.class).listarRoles()).withRel("roles"),
                linkTo(methodOn(RolControllerV2.class).asignarPermiso(rol.getId(), 0)).withRel("asignarPermiso"),
                linkTo(methodOn(RolControllerV2.class).desactivarRol(rol.getId())).withRel("desactivar"),
                linkTo(methodOn(RolControllerV2.class).activarRol(rol.getId())).withRel("activar"));
    }
}
