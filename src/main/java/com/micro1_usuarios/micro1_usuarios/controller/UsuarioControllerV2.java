package com.micro1_usuarios.micro1_usuarios.controller;

import com.micro1_usuarios.micro1_usuarios.assemblers.UsuarioModelAssembler;
import com.micro1_usuarios.micro1_usuarios.model.Usuario;
import com.micro1_usuarios.micro1_usuarios.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/v2/usuarios")
public class UsuarioControllerV2 {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioModelAssembler assembler;

    // Crear un nuevo usuario (POST)
    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> crearUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
        return ResponseEntity
                .created(linkTo(methodOn(UsuarioControllerV2.class).obtenerUsuarioPorId(nuevoUsuario.getId())).toUri())
                .body(assembler.toModel(nuevoUsuario));
    }

    // Listar todos los usuarios (GET)
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> listarUsuarios() {
        List<EntityModel<Usuario>> usuarios = usuarioService.listarUsuarios().stream()
                .map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(CollectionModel.of(
                usuarios,
                linkTo(methodOn(UsuarioControllerV2.class).listarUsuarios()).withSelfRel()
        ));
    }

    // Obtener un usuario por ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> obtenerUsuarioPorId(@PathVariable int id) {
        Optional<Usuario> usuarioOpt = usuarioService.listarUsuarioPorId(id);

        return usuarioOpt
                .map(usuario -> ResponseEntity.ok(assembler.toModel(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar los datos de un usuario (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> actualizarUsuario(@PathVariable int id, @RequestBody Usuario usuarioActualizado) {
        Usuario actualizado = usuarioService.actualizarUsuario(id, usuarioActualizado);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    // Asignar un rol a un usuario (PUT)
    @PutMapping("/{usuarioId}/rol/{rolId}")
    public ResponseEntity<EntityModel<Usuario>> asignarRol(@PathVariable int usuarioId, @PathVariable int rolId) {
        Usuario usuarioActualizado = usuarioService.asignarRol(usuarioId, rolId);
        return ResponseEntity.ok(assembler.toModel(usuarioActualizado));
    }

    // Desactivar un usuario (PUT)
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarUsuario(@PathVariable int id) {
        usuarioService.desactivarUsuario(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // Activar un usuario (PUT)
    @PutMapping("/{id}/activar")
    public ResponseEntity<Void> activarUsuario(@PathVariable int id) {
        usuarioService.activarUsuario(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
