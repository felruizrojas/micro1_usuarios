package com.micro1_usuarios.micro1_usuarios.controller;

import com.micro1_usuarios.micro1_usuarios.assemblers.UsuarioModelAssembler;
import com.micro1_usuarios.micro1_usuarios.model.Usuario;
import com.micro1_usuarios.micro1_usuarios.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/usuarios")
public class UsuarioControllerV2 {

        @Autowired
        private UsuarioService usuarioService;

        @Autowired
        private UsuarioModelAssembler assembler;

        /**
         * Crea un nuevo usuario en el sistema.
         * Retorna un EntityModel con enlaces HATEOAS.
         */
        @Operation(summary = "Crear un nuevo usuario")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
        })
        @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<EntityModel<Usuario>> crearUsuario(@RequestBody Usuario usuario) {
                Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
                return ResponseEntity
                                .created(linkTo(methodOn(UsuarioControllerV2.class)
                                                .obtenerUsuarioPorId(nuevoUsuario.getId())).toUri())
                                .body(assembler.toModel(nuevoUsuario));
        }

        /**
         * Lista todos los usuarios del sistema.
         * Retorna una colección HATEOAS.
         */
        @Operation(summary = "Obtener la lista de todos los usuarios")
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida")
        @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
        public CollectionModel<EntityModel<Usuario>> listarUsuarios() {
                List<EntityModel<Usuario>> usuarios = usuarioService.listarUsuarios().stream()
                                .map(assembler::toModel)
                                .collect(Collectors.toList());

                return CollectionModel.of(usuarios,
                                linkTo(methodOn(UsuarioControllerV2.class).listarUsuarios()).withSelfRel());
        }

        /**
         * Obtiene un usuario específico por su ID.
         * Lanza excepción si no se encuentra.
         */
        @Operation(summary = "Obtener un usuario por su ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        })
        @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
        public EntityModel<Usuario> obtenerUsuarioPorId(@PathVariable int id) {
                Usuario usuario = usuarioService.listarUsuarioPorId(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Usuario no encontrado"));
                return assembler.toModel(usuario);
        }

        /**
         * Actualiza los datos de un usuario existente.
         */
        @Operation(summary = "Actualizar un usuario existente")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        })
        @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<EntityModel<Usuario>> actualizarUsuario(@PathVariable int id,
                        @RequestBody Usuario usuarioActualizado) {
                Usuario actualizado = usuarioService.actualizarUsuario(id, usuarioActualizado);
                return ResponseEntity.ok(assembler.toModel(actualizado));
        }

        /**
         * Asigna un rol existente a un usuario específico.
         */
        @Operation(summary = "Asignar un rol a un usuario")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Rol asignado exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Usuario o rol no encontrado")
        })
        @PutMapping(value = "/{usuarioId}/rol/{rolId}", produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<EntityModel<Usuario>> asignarRol(@PathVariable int usuarioId, @PathVariable int rolId) {
                Usuario usuarioActualizado = usuarioService.asignarRol(usuarioId, rolId);
                return ResponseEntity.ok(assembler.toModel(usuarioActualizado));
        }

        /**
         * Desactiva un usuario (soft delete o cambio de estado).
         */
        @Operation(summary = "Desactivar un usuario")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Usuario desactivado exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        })
        @PutMapping(value = "/{id}/desactivar", produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<Void> desactivarUsuario(@PathVariable int id) {
                usuarioService.desactivarUsuario(id);
                return ResponseEntity.noContent().build();
        }

        /**
         * Activa un usuario previamente desactivado.
         */
        @Operation(summary = "Activar un usuario")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Usuario activado exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        })
        @PutMapping(value = "/{id}/activar", produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<Void> activarUsuario(@PathVariable int id) {
                usuarioService.activarUsuario(id);
                return ResponseEntity.noContent().build();
        }
}

/*
 * Endpoints – UsuarioControllerV2 (con localhost:8080)
 * POST http://localhost:8080/api/v2/usuarios
 * 
 * GET http://localhost:8080/api/v2/usuarios
 * 
 * GET http://localhost:8080/api/v2/usuarios/{id}
 * 
 * PUT http://localhost:8080/api/v2/usuarios/{id}
 * 
 * PUT http://localhost:8080/api/v2/usuarios/{usuarioId}/rol/{rolId}
 * 
 * PUT http://localhost:8080/api/v2/usuarios/{id}/desactivar
 * 
 * PUT http://localhost:8080/api/v2/usuarios/{id}/activar
 * 
 */