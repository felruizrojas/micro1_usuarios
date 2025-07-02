package com.micro1_usuarios.micro1_usuarios.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.micro1_usuarios.micro1_usuarios.controller.PermisoControllerV2;
import com.micro1_usuarios.micro1_usuarios.model.Permiso;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

@Component
public class PermisoModelAssembler implements RepresentationModelAssembler<Permiso, EntityModel<Permiso>> {

    @Override
    @NonNull
    public EntityModel<Permiso> toModel(@NonNull Permiso permiso) {
        return EntityModel.of(permiso,
                linkTo(methodOn(PermisoControllerV2.class).obtenerPermisoPorId(permiso.getId())).withSelfRel(),
                linkTo(methodOn(PermisoControllerV2.class).listarPermisos()).withRel("permisos"),
                linkTo(methodOn(PermisoControllerV2.class).desactivarPermiso(permiso.getId())).withRel("desactivar"),
                linkTo(methodOn(PermisoControllerV2.class).activarPermiso(permiso.getId())).withRel("activar"));
    }
}
