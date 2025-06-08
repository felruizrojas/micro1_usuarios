package com.micro1_usuarios.micro1_usuarios.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.micro1_usuarios.micro1_usuarios.model.Permiso;
import com.micro1_usuarios.micro1_usuarios.model.Usuario;
import com.micro1_usuarios.micro1_usuarios.service.PermisoService;

@Controller

public class PermisoController {

    @Autowired
    private PermisoService permisoService;

    @PostMapping
    public ResponseEntity<Permiso> crearPermiso(@RequestBody Permiso permiso) {
        Permiso nuevoPermiso = permisoService.crearPermiso(permiso);
        return ResponseEntity.ok(nuevoPermiso);
    }

    @GetMapping
    public List<Permiso> listarPermisos() {
        return permisoService.listarPermisos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permiso> obtenerPermisoPorId(@PathVariable int id) {
        Optional<Permiso> permiso = permisoService.listarPermisoPorId(id);
        return permiso.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Permiso> actualizarPermiso(@PathVariable int id, @RequestBody Permiso permisoActualizado) {
        Permiso actualizado = permisoService.actualizarPermiso(id, permisoActualizado);
        return ResponseEntity.ok(actualizado);
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarPermiso(@PathVariable int id) {
        permisoService.desactivarPermiso(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
