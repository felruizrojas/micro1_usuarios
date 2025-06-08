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

import com.micro1_usuarios.micro1_usuarios.model.Rol;
import com.micro1_usuarios.micro1_usuarios.service.RolService;

@Controller

public class RolController {

    @Autowired
    private RolService rolService;

    @PostMapping
    public ResponseEntity<Rol> crearRol(@RequestBody Rol rol) {
        Rol nuevoRol = rolService.crearRol(rol);
        return ResponseEntity.ok(nuevoRol);
    }

    @GetMapping
    public List<Rol> listarRoles() {
        return rolService.listarRoles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rol> obtenerRolPorId(@PathVariable int id) {
        Optional<Rol> rol = rolService.listarRolPorId(id);
        return rol.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rol> actualizarRol(@PathVariable int id, @RequestBody Rol rolActualizado) {
        Rol actualizado = rolService.actualizarRol(id, rolActualizado);
        return ResponseEntity.ok(actualizado);
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarRol(@PathVariable int id) {
        rolService.desactivarRol(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
