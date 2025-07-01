package com.micro1_usuarios.micro1_usuarios.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.micro1_usuarios.micro1_usuarios.controller.PermisoController;
import com.micro1_usuarios.micro1_usuarios.model.Permiso;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class PermisoModelAssembler implements RepresentationModelAssembler<Permiso, EntityModel<Permiso>> {

    @Override
    @NonNull
    public EntityModel<Permiso> toModel(@NonNull Permiso permiso) {
        return EntityModel.of(permiso,
                linkTo(methodOn(PermisoController.class).obtenerPermisoPorId(permiso.getId())).withSelfRel(),
                linkTo(methodOn(PermisoController.class).listarPermisos()).withRel("permisos"),
                linkTo(methodOn(PermisoController.class).desactivarPermiso(permiso.getId())).withRel("desactivar"),
                linkTo(methodOn(PermisoController.class).activarPermiso(permiso.getId())).withRel("activar"));
    }
}
