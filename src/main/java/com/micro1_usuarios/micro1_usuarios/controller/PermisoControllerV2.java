package com.micro1_usuarios.micro1_usuarios.controller;

import com.micro1_usuarios.micro1_usuarios.assemblers.PermisoModelAssembler;
import com.micro1_usuarios.micro1_usuarios.model.Permiso;
import com.micro1_usuarios.micro1_usuarios.service.PermisoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/permisos")
public class PermisoControllerV2 {

        @Autowired
        private PermisoService permisoService;

        @Autowired
        private PermisoModelAssembler assembler;

        // Crear un nuevo permiso
        @Operation(summary = "Crear un nuevo permiso")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Permiso creado exitosamente"),
                        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
        })
        @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<EntityModel<Permiso>> crearPermiso(@RequestBody Permiso permiso) {
                Permiso nuevoPermiso = permisoService.crearPermiso(permiso);
                return ResponseEntity
                                .created(linkTo(methodOn(PermisoControllerV2.class)
                                                .obtenerPermisoPorId(nuevoPermiso.getId())).toUri())
                                .body(assembler.toModel(nuevoPermiso));
        }

        // Listar todos los permisos
        @Operation(summary = "Listar todos los permisos")
        @ApiResponse(responseCode = "200", description = "Lista de permisos obtenida")
        @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
        public CollectionModel<EntityModel<Permiso>> listarPermisos() {
                List<EntityModel<Permiso>> permisos = permisoService.listarPermisos().stream()
                                .map(assembler::toModel)
                                .collect(Collectors.toList());

                return CollectionModel.of(permisos,
                                linkTo(methodOn(PermisoControllerV2.class).listarPermisos()).withSelfRel());
        }

        // Obtener un permiso por ID
        @Operation(summary = "Obtener un permiso por ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Permiso encontrado"),
                        @ApiResponse(responseCode = "404", description = "Permiso no encontrado")
        })
        @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
        public EntityModel<Permiso> obtenerPermisoPorId(@PathVariable int id) {
                Permiso permiso = permisoService.listarPermisoPorId(id);
                return assembler.toModel(permiso);
        }

        // Actualizar un permiso
        @Operation(summary = "Actualizar un permiso")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Permiso actualizado"),
                        @ApiResponse(responseCode = "404", description = "Permiso no encontrado")
        })
        @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<EntityModel<Permiso>> actualizarPermiso(@PathVariable int id,
                        @RequestBody Permiso permisoActualizado) {
                Permiso actualizado = permisoService.actualizarPermiso(id, permisoActualizado);
                return ResponseEntity.ok(assembler.toModel(actualizado));
        }

        // Desactivar un permiso
        @Operation(summary = "Desactivar un permiso")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Permiso desactivado"),
                        @ApiResponse(responseCode = "404", description = "Permiso no encontrado")
        })
        @PutMapping(value = "/{id}/desactivar", produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<Void> desactivarPermiso(@PathVariable int id) {
                permisoService.desactivarPermiso(id);
                return ResponseEntity.noContent().build();
        }

        // Activar un permiso
        @Operation(summary = "Activar un permiso")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Permiso activado"),
                        @ApiResponse(responseCode = "404", description = "Permiso no encontrado")
        })
        @PutMapping(value = "/{id}/activar", produces = MediaTypes.HAL_JSON_VALUE)
        public ResponseEntity<Void> activarPermiso(@PathVariable int id) {
                permisoService.activarPermiso(id);
                return ResponseEntity.noContent().build();
        }
}

/*
 * POST http://localhost:8080/api/v2/permisos
 * GET http://localhost:8080/api/v2/permisos
 * GET http://localhost:8080/api/v2/permisos/{id}
 * PUT http://localhost:8080/api/v2/permisos/{id}
 * PUT http://localhost:8080/api/v2/permisos/{id}/desactivar
 * PUT http://localhost:8080/api/v2/permisos/{id}/activar
 */