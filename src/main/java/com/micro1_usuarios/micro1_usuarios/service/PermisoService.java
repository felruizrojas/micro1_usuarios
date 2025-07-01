package com.micro1_usuarios.micro1_usuarios.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.micro1_usuarios.micro1_usuarios.model.Permiso;
import com.micro1_usuarios.micro1_usuarios.repository.PermisoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PermisoService {

    private final PermisoRepository permisoRepository;

    public PermisoService(PermisoRepository permisoRepository) {
        this.permisoRepository = permisoRepository;
    }

    public Permiso crearPermiso(Permiso permiso) {
        return permisoRepository.save(permiso);
    }

    public List<Permiso> listarPermisos() {
        return permisoRepository.findAll();
    }

    public Permiso listarPermisoPorId(int id) {
        return permisoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Permiso no encontrado con id " + id));
    }

    public Permiso actualizarPermiso(int id, Permiso permisoActualizado) {
        return permisoRepository.findById(id).map(permiso -> {
            if (permisoActualizado.getNombrePermiso() != null)
                permiso.setNombrePermiso(permisoActualizado.getNombrePermiso());
            return permisoRepository.save(permiso);
        }).orElseThrow(() -> new EntityNotFoundException("Permiso no encontrado con id " + id));
    }


    public void desactivarPermiso(int id) {
        Permiso permiso = permisoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permiso no encontrado con id " + id));
        permiso.setPermisoActivo(false);
        permisoRepository.save(permiso);
    }

    public void activarPermiso(int id) {
        Permiso permiso = permisoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permiso no encontrado con id " + id));
        permiso.setPermisoActivo(true);
        permisoRepository.save(permiso);
    }
}
