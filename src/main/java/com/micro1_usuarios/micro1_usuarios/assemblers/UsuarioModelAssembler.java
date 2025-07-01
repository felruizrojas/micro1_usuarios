package com.micro1_usuarios.micro1_usuarios.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.micro1_usuarios.micro1_usuarios.controller.UsuarioController;
import com.micro1_usuarios.micro1_usuarios.model.Usuario;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @Override
    @NonNull
    public EntityModel<Usuario> toModel(@NonNull Usuario usuario) {
        return EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).obtenerUsuarioPorId(usuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarUsuarios()).withRel("usuarios"),
                linkTo(methodOn(UsuarioController.class).asignarRol(usuario.getId(), 0)).withRel("asignarRol"),
                linkTo(methodOn(UsuarioController.class).desactivarUsuario(usuario.getId())).withRel("desactivar"),
                linkTo(methodOn(UsuarioController.class).activarUsuario(usuario.getId())).withRel("activar"));
    }
}
