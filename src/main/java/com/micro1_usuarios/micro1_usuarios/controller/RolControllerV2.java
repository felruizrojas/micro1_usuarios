package com.micro1_usuarios.micro1_usuarios.controller;

import com.micro1_usuarios.micro1_usuarios.assemblers.RolModelAssembler;
import com.micro1_usuarios.micro1_usuarios.model.Rol;
import com.micro1_usuarios.micro1_usuarios.service.RolService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/v2/roles")
public class RolControllerV2 {

    @Autowired
    private RolService rolService;

    @Autowired
    private RolModelAssembler assembler;

    // Crear un nuevo rol (POST)
    @PostMapping
    public ResponseEntity<EntityModel<Rol>> crearRol(@RequestBody Rol rol) {
        Rol nuevoRol = rolService.crearRol(rol);
        return ResponseEntity
                .created(linkTo(methodOn(RolControllerV2.class).listarRolPorId(nuevoRol.getId())).toUri())
                .body(assembler.toModel(nuevoRol));
    }

    // Listar todos los roles (GET)
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Rol>>> listarRoles() {
        List<EntityModel<Rol>> roles = rolService.obtenerRoles().stream()
                .map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(CollectionModel.of(
                roles,
                linkTo(methodOn(RolControllerV2.class).listarRoles()).withSelfRel()));
    }

    // Obtener un rol por ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Rol>> listarRolPorId(@PathVariable int id) {
        Optional<Rol> rolOpt = rolService.obtenerRolPorId(id);
        return rolOpt
                .map(rol -> ResponseEntity.ok(assembler.toModel(rol)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar los datos de un rol (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Rol>> actualizarRol(@PathVariable int id, @RequestBody Rol nuevoRol) {
        Rol actualizado = rolService.actualizarRol(id, nuevoRol);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    // Asignar un permiso a un rol (PUT)
    @PutMapping("/{rolId}/permiso/{permisoId}")
    public ResponseEntity<EntityModel<Rol>> asignarPermiso(@PathVariable int rolId, @PathVariable int permisoId) {
        Rol rolActualizado = rolService.asignarPermiso(rolId, permisoId);
        return ResponseEntity.ok(assembler.toModel(rolActualizado));
    }

    // Desactivar un rol (PUT)
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarRol(@PathVariable int id) {
        rolService.desactivarRol(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // Activar un rol (PUT)
    @PutMapping("/{id}/activar")
    public ResponseEntity<Void> activarRol(@PathVariable int id) {
        rolService.activarRol(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
