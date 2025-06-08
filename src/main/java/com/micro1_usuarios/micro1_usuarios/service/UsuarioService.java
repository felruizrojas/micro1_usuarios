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
            usuario.setUser(usuarioActualizado.getUser());
            usuario.setPass(usuarioActualizado.getPass());
            usuario.setUsuarioActivo(usuarioActualizado.isUsuarioActivo());
            usuario.setPNombre(usuarioActualizado.getPNombre());
            usuario.setSNombre(usuarioActualizado.getSNombre());
            usuario.setPApellido(usuarioActualizado.getPApellido());
            usuario.setSApellido(usuarioActualizado.getSApellido());
            usuario.setCorreo(usuarioActualizado.getCorreo());
            usuario.setDireccion(usuarioActualizado.getDireccion());
            usuario.setCiudad(usuarioActualizado.getCiudad());
            usuario.setRegion(usuarioActualizado.getRegion());
            return usuarioRepository.save(usuario);
        }).orElseGet(() -> {
            usuarioActualizado.setId(id);
            return usuarioRepository.save(usuarioActualizado);
        });
    }

    public Usuario asignarRol(int usuarioId, int rolId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.setRol(rol);
        return usuarioRepository.save(usuario);
    }

    /* asignar un rol con un nombre específico:
    public Usuario asignarRolPorNombre(int usuarioId, String nombreRol) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Rol rol = rolRepository.findByNombreRol(nombreRol);
        if (rol == null) {
            throw new RuntimeException("Rol no encontrado");
        }
        usuario.setRol(rol);
        return usuarioRepository.save(usuario);
    }
    */

    public void desactivarUsuario(int id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setUsuarioActivo(false); //
        usuarioRepository.save(usuario);
    }
}
