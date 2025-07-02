package com.micro1_usuarios.micro1_usuarios.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.micro1_usuarios.micro1_usuarios.model.Usuario;
import com.micro1_usuarios.micro1_usuarios.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")

public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /*
     * Controlador REST para gestionar usuarios.
     * Proporciona endpoints para:
     */

    // Crear un nuevo usuario (POST)
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }

    // Listar todos los usuarios (GET)
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    // Obtener un usuario por ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable int id) {
        Optional<Usuario> usuario = usuarioService.listarUsuarioPorId(id);
        return usuario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar los datos de un usuario (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable int id, @RequestBody Usuario usuarioActualizado) {
        Usuario actualizado = usuarioService.actualizarUsuario(id, usuarioActualizado);
        return ResponseEntity.ok(actualizado);
    }

    // Asignar un rol a un usuario (PUT)
    @PutMapping("/{usuarioId}/rol/{rolId}") // PUT /usuarios/5/rol/2
    public ResponseEntity<Usuario> asignarRol(@PathVariable int usuarioId, @PathVariable int rolId) {
        Usuario usuarioActualizado = usuarioService.asignarRol(usuarioId, rolId);
        return ResponseEntity.ok(usuarioActualizado);
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
