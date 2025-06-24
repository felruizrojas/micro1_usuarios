package com.micro1_usuarios.micro1_usuarios.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro1_usuarios.micro1_usuarios.model.Permiso;
import com.micro1_usuarios.micro1_usuarios.repository.PermisoRepository;

@Service

public class PermisoService {

    @Autowired
    private PermisoRepository permisoRepository;

    public Permiso crearPermiso(Permiso permiso) {
        // permiso.setPermisoActivo(true);
        return permisoRepository.save(permiso);
    }

    public List<Permiso> listarPermisos() {
        return permisoRepository.findAll();
    }

    public Optional<Permiso> listarPermisoPorId(int id) {
        return permisoRepository.findById(id);
    }

    public Permiso actualizarPermiso(int id, Permiso permisoActualizado) {
        return permisoRepository.findById(id).map(permiso -> {
            if (permisoActualizado.getNombrePermiso() != null)
                permiso.setNombrePermiso(permisoActualizado.getNombrePermiso());
            return permisoRepository.save(permiso);
        }).orElseGet(() -> {
            permisoActualizado.setId(id);
            return permisoRepository.save(permisoActualizado);
        });
    }

    public void desactivarPermiso(int id) {
        Permiso permiso = permisoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));
        permiso.setPermisoActivo(false); //
        permisoRepository.save(permiso);
    }

    public void activarPermiso(int id) {
        Permiso permiso = permisoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));
        permiso.setPermisoActivo(true); //
        permisoRepository.save(permiso);
    }
}
