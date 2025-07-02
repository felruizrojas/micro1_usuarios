package com.micro1_usuarios.micro1_usuarios.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro1_usuarios.micro1_usuarios.model.Permiso;
import com.micro1_usuarios.micro1_usuarios.model.Rol;
import com.micro1_usuarios.micro1_usuarios.repository.PermisoRepository;
import com.micro1_usuarios.micro1_usuarios.repository.RolRepository;

import jakarta.persistence.EntityNotFoundException;

@Service

public class RolService {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PermisoRepository permisoRepository;

    public Rol crearRol(Rol rol) {
        if (rol.getPermisos() == null) {
            rol.setPermisos(new ArrayList<>());
        } else {
            // Establecer la relación inversa en cada permiso
            for (Permiso permiso : rol.getPermisos()) {
                permiso.setRol(rol);
            }
        }
        return rolRepository.save(rol);
    }

    public List<Rol> obtenerRoles() {
        return rolRepository.findAll();
    }

    public Optional<Rol> obtenerRolPorId(int id) {
        return rolRepository.findById(id);
    }

    public Rol actualizarRol(int id, Rol nuevoRol) {
        return rolRepository.findById(id).map(rolExistente -> {
            if (nuevoRol.getNombreRol() != null)
                rolExistente.setNombreRol(nuevoRol.getNombreRol());
            return rolRepository.save(rolExistente);
        }).orElseGet(() -> {
            nuevoRol.setId(id);
            nuevoRol.setRolActivo(true);
            return rolRepository.save(nuevoRol);
        });
    }

    public Rol asignarPermiso(int rolId, int permisoId) {
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + rolId));

        Permiso permiso = permisoRepository.findById(permisoId)
                .orElseThrow(() -> new EntityNotFoundException("Permiso no encontrado con ID: " + permisoId));

        // Evita reasignar si ya está asignado al mismo rol
        if (permiso.getRol() == null || !permiso.getRol().equals(rol)) {
            permiso.setRol(rol);
            permisoRepository.save(permiso);
        }

        return rol;
    }

    public void desactivarRol(int id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + id));
        rol.setRolActivo(false); //
        rolRepository.save(rol);
    }

    public void activarRol(int id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + id));
        rol.setRolActivo(true); //
        rolRepository.save(rol);
    }
}
