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

@Service

public class RolService {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PermisoRepository permisoRepository;

    public Rol crearRol(Rol rol) {
        if (rol.getPermisos() == null) {
            rol.setPermisos(new ArrayList<>());
        }
        return rolRepository.save(rol);
    }

    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }

    public Optional<Rol> listarRolPorId(int id) {
        return rolRepository.findById(id);
    }

    public Rol actualizarRol(int id, Rol rolActualizado) {
        return rolRepository.findById(id).map(rol -> {
            rol.setNombreRol(rolActualizado.getNombreRol());
            // rol.setRolActivo(rolActualizado.isRolActivo());
            return rolRepository.save(rol);
        }).orElseGet(() -> {
            rolActualizado.setId(id);
            return rolRepository.save(rolActualizado);
        });
    }

    public Rol asignarPermiso(int rolId, int permisoId) {
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        Permiso permiso = permisoRepository.findById(permisoId)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));
        permiso.setRol(rol);
        permisoRepository.save(permiso);
        return rol;
    }

    public void desactivarRol(int id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        rol.setRolActivo(false); //
        rolRepository.save(rol);
    }
}
