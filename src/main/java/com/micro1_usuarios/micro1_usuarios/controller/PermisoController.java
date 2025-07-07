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

import com.micro1_usuarios.micro1_usuarios.model.Permiso;
import com.micro1_usuarios.micro1_usuarios.service.PermisoService;

@RestController
@RequestMapping("/permisos")

public class PermisoController {

    @Autowired
    private PermisoService permisoService;

    /*
     * Controlador REST para gestionar permisos.
     * Proporciona endpoints para:
     */

    // Crear un nuevo permiso (POST)
    @PostMapping
    public ResponseEntity<Permiso> crearPermiso(@RequestBody Permiso permiso) {
        Permiso nuevoPermiso = permisoService.crearPermiso(permiso);
        return ResponseEntity.ok(nuevoPermiso);
    }

    // Listar todos los permisos (GET)
    @GetMapping
    public List<Permiso> listarPermisos() {
        return permisoService.listarPermisos();
    }

    // Obtener un permiso por ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Permiso> obtenerPermisoPorId(@PathVariable int id) {
        Optional<Permiso> permiso = Optional.ofNullable(permisoService.listarPermisoPorId(id));
        return permiso.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar los datos de un permiso (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Permiso> actualizarPermiso(@PathVariable int id, @RequestBody Permiso permisoActualizado) {
        Permiso actualizado = permisoService.actualizarPermiso(id, permisoActualizado);
        return ResponseEntity.ok(actualizado);
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
