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

                // 1. Obtener usuario por ID (self)
                linkTo(methodOn(UsuarioController.class).obtenerUsuarioPorId(usuario.getId())).withSelfRel(),

                // 2. Listar todos los usuarios
                linkTo(methodOn(UsuarioController.class).listarUsuarios()).withRel("listarTodos"),

                // 3. Crear un nuevo usuario
                linkTo(methodOn(UsuarioController.class).crearUsuario(null)).withRel("crearUsuario"),

                // 4. Actualizar usuario
                linkTo(methodOn(UsuarioController.class).actualizarUsuario(usuario.getId(), usuario)).withRel("actualizarUsuario"),

                // 5. Asignar rol al usuario (usamos rolId ficticio como ejemplo)
                linkTo(methodOn(UsuarioController.class).asignarRol(usuario.getId(), 0)).withRel("asignarRol")
                    .withTitle("Requiere rolId válido"),

                // 6. Desactivar usuario
                linkTo(methodOn(UsuarioController.class).desactivarUsuario(usuario.getId())).withRel("desactivarUsuario"),

                // 7. Activar usuario
                linkTo(methodOn(UsuarioController.class).activarUsuario(usuario.getId())).withRel("activarUsuario")
        );
    }

    //http://localhost:8080/swagger-ui/index.html
}
