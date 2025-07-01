package com.micro1_usuarios.micro1_usuarios.controller;

import com.micro1_usuarios.micro1_usuarios.assemblers.PermisoModelAssembler;
import com.micro1_usuarios.micro1_usuarios.model.Permiso;
import com.micro1_usuarios.micro1_usuarios.service.PermisoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/v2/permisos")
public class PermisoControllerV2 {

    @Autowired
    private PermisoService permisoService;

    @Autowired
    private PermisoModelAssembler assembler;

    // Crear un nuevo permiso (POST)
    @PostMapping
    public ResponseEntity<EntityModel<Permiso>> crearPermiso(@RequestBody Permiso permiso) {
        Permiso nuevoPermiso = permisoService.crearPermiso(permiso);
        return ResponseEntity
                .created(linkTo(methodOn(PermisoControllerV2.class).obtenerPermisoPorId(nuevoPermiso.getId())).toUri())
                .body(assembler.toModel(nuevoPermiso));
    }

    // Listar todos los permisos (GET)
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Permiso>>> listarPermisos() {
        List<EntityModel<Permiso>> permisos = permisoService.listarPermisos().stream()
                .map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(CollectionModel.of(
                permisos,
                linkTo(methodOn(PermisoControllerV2.class).listarPermisos()).withSelfRel()));
    }

    // Obtener un permiso por ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Permiso>> obtenerPermisoPorId(@PathVariable int id) {
        Optional<Permiso> permiso = Optional.ofNullable(permisoService.listarPermisoPorId(id));
        return permiso
                .map(p -> ResponseEntity.ok(assembler.toModel(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar los datos de un permiso (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Permiso>> actualizarPermiso(@PathVariable int id,
            @RequestBody Permiso permisoActualizado) {
        Permiso actualizado = permisoService.actualizarPermiso(id, permisoActualizado);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    // Desactivar un permiso (PUT)
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarPermiso(@PathVariable int id) {
        permisoService.desactivarPermiso(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // Activar un permiso (PUT)
    @PutMapping("/{id}/activar")
    public ResponseEntity<Void> activarPermiso(@PathVariable int id) {
        permisoService.activarPermiso(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
