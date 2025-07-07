package com.micro1_usuarios.micro1_usuarios.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro1_usuarios.micro1_usuarios.model.Rol;
import com.micro1_usuarios.micro1_usuarios.model.Usuario;
import com.micro1_usuarios.micro1_usuarios.repository.RolRepository;
import com.micro1_usuarios.micro1_usuarios.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    public Usuario crearUsuario(Usuario usuario) {
        if (usuario.getUser() == null || usuario.getUser().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }
        if (usuarioRepository.findByUser(usuario.getUser()).isPresent()) {
            throw new RuntimeException("Ya existe un usuario con ese nombre.");
        }
        if (usuario.getRol() == null || usuario.getRol().getId() == 0) {
            throw new RuntimeException("Rol no encontrado");
        }
        Rol rol = rolRepository.findById(usuario.getRol().getId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        if (!rol.isRolActivo()) {
            throw new RuntimeException("No se puede usar un rol desactivado al crear el usuario");
        }
        usuario.setRol(rol);
        usuario.setUsuarioActivo(true);
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> listarUsuarioPorId(int id) {
        return usuarioRepository.findById(id);
    }

    public Usuario actualizarUsuario(int id, Usuario usuarioActualizado) {
        return usuarioRepository.findById(id).map(usuario -> {
            if (usuarioActualizado.getUser() != null)
                usuario.setUser(usuarioActualizado.getUser());
            if (usuarioActualizado.getPass() != null)
                usuario.setPass(usuarioActualizado.getPass());
            if (usuarioActualizado.getPrimerNombre() != null)
                usuario.setPrimerNombre(usuarioActualizado.getPrimerNombre());
            if (usuarioActualizado.getPrimerApellido() != null)
                usuario.setPrimerApellido(usuarioActualizado.getPrimerApellido());
            if (usuarioActualizado.getCorreo() != null)
                usuario.setCorreo(usuarioActualizado.getCorreo());
            if (usuarioActualizado.getDireccion() != null)
                usuario.setDireccion(usuarioActualizado.getDireccion());
            if (usuarioActualizado.getCiudad() != null)
                usuario.setCiudad(usuarioActualizado.getCiudad());
            if (usuarioActualizado.getRegion() != null)
                usuario.setRegion(usuarioActualizado.getRegion());
            if (usuarioActualizado.getRol() != null && usuarioActualizado.getRol().getId() != 0) {
                Rol rol = rolRepository.findById(usuarioActualizado.getRol().getId())
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
                if (!rol.isRolActivo()) {
                    throw new RuntimeException("No se puede asignar un rol desactivado");
                }
                usuario.setRol(rol);
            }
            return usuarioRepository.save(usuario);
        }).orElseGet(() -> {
            usuarioActualizado.setId(id);
            usuarioActualizado.setUsuarioActivo(true);
            return usuarioRepository.save(usuarioActualizado);
        });
    }

    public Usuario asignarRol(int usuarioId, int rolId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        if (!rol.isRolActivo()) {
            throw new RuntimeException("No se puede asignar un rol desactivado");
        }
        usuario.setRol(rol);
        return usuarioRepository.save(usuario);
    }

    public void desactivarUsuario(int id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setUsuarioActivo(false);
        usuarioRepository.save(usuario);
    }

    public void activarUsuario(int id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setUsuarioActivo(true);
        usuarioRepository.save(usuario);
    }

    
}
