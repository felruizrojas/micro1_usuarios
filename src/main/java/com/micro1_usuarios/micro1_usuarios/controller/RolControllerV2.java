package com.micro1_usuarios.micro1_usuarios.controller;

import com.micro1_usuarios.micro1_usuarios.assemblers.RolModelAssembler;
import com.micro1_usuarios.micro1_usuarios.model.Rol;
import com.micro1_usuarios.micro1_usuarios.service.RolService;

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
@RequestMapping("/api/v2/roles") // Ruta base para todos los endpoints relacionados con roles
public class RolControllerV2 {

        @Autowired
        private RolService rolService; // Servicio que maneja la lógica de negocio para roles

        @Autowired
        private RolModelAssembler assembler; // Ensamblador que convierte objetos Rol en modelos HATEOAS

        // Endpoint para crear un nuevo rol
        @Operation(summary = "Crear un nuevo rol")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Rol creado exitosamente"),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
        })
        @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<EntityModel<Rol>> crearRol(@RequestBody Rol rol) {
                Rol nuevoRol = rolService.crearRol(rol); // Crea el rol
                return ResponseEntity
                                .created(linkTo(methodOn(RolControllerV2.class).obtenerRolPorId(nuevoRol.getId()))
                                                .toUri())
                                .body(assembler.toModel(nuevoRol)); // Retorna el rol creado con enlaces HATEOAS
        }

        // Endpoint para listar todos los roles
        @Operation(summary = "Listar todos los roles")
        @ApiResponse(responseCode = "200", description = "Lista de roles obtenida")
        @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
        public CollectionModel<EntityModel<Rol>> listarRoles() {
                List<EntityModel<Rol>> roles = rolService.obtenerRoles().stream()
                                .map(assembler::toModel)
                                .collect(Collectors.toList()); // Transforma cada rol en un modelo HATEOAS

                return CollectionModel.of(roles,
                                linkTo(methodOn(RolControllerV2.class).listarRoles()).withSelfRel()); // Retorna la
                                                                                                      // colección con
                                                                                                      // enlace propio
        }

        // Endpoint para obtener un rol específico por ID
        @Operation(summary = "Obtener un rol por ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Rol encontrado"),
                        @ApiResponse(responseCode = "404", description = "Rol no encontrado")
        })
        @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
        public EntityModel<Rol> obtenerRolPorId(@PathVariable int id) {
                Rol rol = rolService.obtenerRolPorId(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado")); // Lanza excepción si no
                                                                                               // se encuentra
                return assembler.toModel(rol); // Retorna el modelo HATEOAS del rol
        }

        // Endpoint para actualizar un rol existente
        @Operation(summary = "Actualizar un rol")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Rol actualizado"),
                        @ApiResponse(responseCode = "404", description = "Rol no encontrado")
        })
        @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<EntityModel<Rol>> actualizarRol(@PathVariable int id, @RequestBody Rol nuevoRol) {
                Rol actualizado = rolService.actualizarRol(id, nuevoRol); // Actualiza el rol
                return ResponseEntity.ok(assembler.toModel(actualizado)); // Retorna el modelo actualizado
        }

        // Endpoint para asignar un permiso a un rol
        @Operation(summary = "Asignar permiso a un rol")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Permiso asignado"),
                        @ApiResponse(responseCode = "404", description = "Rol o permiso no encontrado")
        })
        @PutMapping(value = "/{rolId}/permiso/{permisoId}", produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<EntityModel<Rol>> asignarPermiso(@PathVariable int rolId, @PathVariable int permisoId) {
                Rol actualizado = rolService.asignarPermiso(rolId, permisoId); // Asigna el permiso al rol
                return ResponseEntity.ok(assembler.toModel(actualizado)); // Retorna el rol con permiso asignado
        }

        // Endpoint para desactivar un rol (cambio de estado)
        @Operation(summary = "Desactivar un rol")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Rol desactivado"),
                        @ApiResponse(responseCode = "404", description = "Rol no encontrado")
        })
        @PutMapping(value = "/{id}/desactivar", produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<Void> desactivarRol(@PathVariable int id) {
                rolService.desactivarRol(id); // Marca el rol como inactivo
                return ResponseEntity.noContent().build(); // Respuesta sin cuerpo (204)
        }

        // Endpoint para activar un rol previamente desactivado
        @Operation(summary = "Activar un rol")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Rol activado"),
                        @ApiResponse(responseCode = "404", description = "Rol no encontrado")
        })
        @PutMapping(value = "/{id}/activar", produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<Void> activarRol(@PathVariable int id) {
                rolService.activarRol(id); // Marca el rol como activo
                return ResponseEntity.noContent().build(); // Respuesta sin cuerpo (204)
        }
}

/*
 * POST http://localhost:8080/api/v2/roles
 * GET http://localhost:8080/api/v2/roles
 * GET http://localhost:8080/api/v2/roles/{id}
 * PUT http://localhost:8080/api/v2/roles/{id}
 * PUT http://localhost:8080/api/v2/roles/{rolId}/permiso/{permisoId}
 * PUT http://localhost:8080/api/v2/roles/{id}/desactivar
 * PUT http://localhost:8080/api/v2/roles/{id}/activar
 * 
 */