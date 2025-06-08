package com.micro1_usuarios.micro1_usuarios.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro1_usuarios.micro1_usuarios.model.Rol;
import com.micro1_usuarios.micro1_usuarios.repository.RolRepository;

@Service

public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public Rol crearRol(Rol rol) {
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
            rol.setRolActivo(rolActualizado.isRolActivo());
            return rolRepository.save(rol);
        }).orElseGet(() -> {
            rolActualizado.setId(id);
            return rolRepository.save(rolActualizado);
        });
    }

    public void desactivarRol(int id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        rol.setRolActivo(false); //
        rolRepository.save(rol);
    }
}
