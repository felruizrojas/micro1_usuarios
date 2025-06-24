package com.micro1_usuarios.micro1_usuarios.service;

import com.micro1_usuarios.micro1_usuarios.model.Rol;
import com.micro1_usuarios.micro1_usuarios.model.Usuario;
import com.micro1_usuarios.micro1_usuarios.repository.RolRepository;
import com.micro1_usuarios.micro1_usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test // test post --> crear usuario
    void testCrearUsuario() {
        Rol rol = new Rol(
                1, // id
                "ADMIN", // nombreRol
                true, // rolActivo
                Collections.emptyList() // permisos (lista vacía en esta prueba)
        );
        Usuario usuario = new Usuario(
                1, // id
                "usuario123", // user
                "contrasenaSegura", // pass
                true, // usuarioActivo
                "12345678-9", // run
                "Juan", // primerNombre
                "Carlos", // segundoNombre
                "Pérez", // primerApellido
                "González", // segundoApellido
                "juan.perez@example.com", // correo
                "Av. Siempre Viva 123", // direccion
                "Santiago", // ciudad
                "Metropolitana", // region
                rol // rol
        );
        Usuario usuarioCreado = new Usuario(
                1, // id asignado por la BD al guardar
                "usuario123", //
                "contrasenaSegura", //
                true, //
                "12345678-9", //
                "Juan", //
                "Carlos", //
                "Pérez", //
                "González", //
                "juan.perez@example.com", //
                "Av. Siempre Viva 123", //
                "Santiago", //
                "Metropolitana", //
                rol); //
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        when(usuarioRepository.save(usuario)).thenReturn(usuarioCreado);
        Usuario resultado = usuarioService.crearUsuario(usuario);
        assertThat(resultado.getId()).isEqualTo(1);
        assertThat(resultado.getUser()).isEqualTo("usuario123");
        assertThat(resultado.getPass()).isEqualTo("contrasenaSegura");
        assertThat(resultado.isUsuarioActivo()).isTrue();
        assertThat(resultado.getRun()).isEqualTo("12345678-9");
        assertThat(resultado.getPrimerNombre()).isEqualTo("Juan");
        assertThat(resultado.getSegundoNombre()).isEqualTo("Carlos");
        assertThat(resultado.getPrimerApellido()).isEqualTo("Pérez");
        assertThat(resultado.getSegundoApellido()).isEqualTo("González");
        assertThat(resultado.getCorreo()).isEqualTo("juan.perez@example.com");
        assertThat(resultado.getDireccion()).isEqualTo("Av. Siempre Viva 123");
        assertThat(resultado.getCiudad()).isEqualTo("Santiago");
        assertThat(resultado.getRegion()).isEqualTo("Metropolitana");
        assertThat(resultado.getRol().getNombreRol()).isEqualTo("ADMIN");
        verify(usuarioRepository).save(usuario);
    }


    @Test // test get --> listar usuarios
    void testListarUsuarios() {
        Rol rol = new Rol(1, "ADMIN", true, Collections.emptyList());
        Usuario u1 = new Usuario(1, "usuario123", "contrasenaSegura", true, "12345678-9", "Juan", "Carlos", "Pérez", "González", "juan.perez@example.com", "Av. Siempre Viva 123", "Santiago", "Metropolitana", rol);
        Usuario u2 = new Usuario(2, "usuario456", "otraContrasena", true, "98765432-1", "Ana", "Maria", "López", "Fernández", "ana.lopez@example.com", "Av. Siempre Viva 456", "Santiago", "Metropolitana", rol);
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(u1, u2));
        List<Usuario> resultado = usuarioService.listarUsuarios();
        assertThat(resultado).hasSize(2).contains(u1, u2);
        verify(usuarioRepository).findAll();
    }

    @Test // test get --> listar usuario por id
    void testListarUsuarioPorId() {
        Rol rol = new Rol(1, "ADMIN", true, Collections.emptyList());
        Usuario u = new Usuario(1, "usuario123", "contrasenaSegura", true, "12345678-9", "Juan", "Carlos", "Pérez", "González", "juan.perez@example.com", "Av. Siempre Viva 123", "Santiago", "Metropolitana", rol);
        // Simular que el usuario existe en la base de datos
        // y que el método findById devuelve un Optional con el usuario.
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(u));
        // Llamar al método listarUsuarioPorId
        Optional<Usuario> resultado = usuarioService.listarUsuarioPorId(1);
        // Verificar que el resultado es un Optional con el usuario esperado
        assertThat(resultado).isPresent().isEqualTo(Optional.of(u));
        // Verificar que se haya llamado al método findById del repositorio
        assertThat(resultado.get().getId()).isEqualTo(1);
        assertThat(resultado.get().getUser()).isEqualTo("usuario123");
        assertThat(resultado.get().getPass()).isEqualTo("contrasenaSegura");
        assertThat(resultado.get().isUsuarioActivo()).isTrue();
        assertThat(resultado.get().getRun()).isEqualTo("12345678-9");
        assertThat(resultado.get().getPrimerNombre()).isEqualTo("Juan");
        assertThat(resultado.get().getSegundoNombre()).isEqualTo("Carlos");
        assertThat(resultado.get().getPrimerApellido()).isEqualTo("Pérez");
        assertThat(resultado.get().getSegundoApellido()).isEqualTo("González");
        assertThat(resultado.get().getCorreo()).isEqualTo("juan.perez@example.com");
        assertThat(resultado.get().getDireccion()).isEqualTo("Av. Siempre Viva 123");
        assertThat(resultado.get().getCiudad()).isEqualTo("Santiago");
        assertThat(resultado.get().getRegion()).isEqualTo("Metropolitana");
        assertThat(resultado.get().getRol().getNombreRol()).isEqualTo("ADMIN");
        // Verificar que se haya llamado al método findById del repositorio
        verify(usuarioRepository).findById(1);
    }

    @Test // test put --> actualizar usuario 
    void testActualizarUsuario() {
        Rol rol = new Rol(1, "ADMIN", true, Collections.emptyList());
        Usuario usuarioExistente = new Usuario(1, "usuario123", "contrasenaSegura", true, "12345678-9", "Juan", "Carlos", "Pérez", "González", "juan.perez@example.com", "Av. Siempre Viva 123", "Santiago", "Metropolitana", rol);
        Usuario usuarioActualizado = new Usuario(1, "usuario123", "contrasenaSegura", true, "12345678-9", "Juan", "Carlos", "Pérez", "González", "juan.perez@example.com", "Av. Nunca Viva 456", "Santiago", "Metropolitana", rol);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(usuarioActualizado)).thenReturn(usuarioExistente);

        Usuario resultado = usuarioService.actualizarUsuario(1, usuarioActualizado);
        assertThat(resultado.getDireccion()).isEqualTo("Av. Nunca Viva 456");
        verify(usuarioRepository).findById(1);
        verify(usuarioRepository).save(usuarioActualizado);
    }


    @Test // test put --> asignar Rol
    void testAsignarRol() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        Rol rol = new Rol(1, "ADMIN", true, Collections.emptyList());
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        Usuario resultado = usuarioService.asignarRol(1, 1);
        assertThat(resultado.getRol().getNombreRol()).isEqualTo("ADMIN");
        verify(usuarioRepository).save(usuario);
    }

    @Test // desactivarUsuario
    void testDesactivarUsuario() {
        Rol rol = new Rol(1, "ADMIN", true, Collections.emptyList());
        Usuario usuario = new Usuario(1, "usuario123", "contrasenaSegura", true, "12345678-9", "Juan", "Carlos", "Pérez", "González", "juan.perez@example.com", "Av. Siempre Viva 123", "Santiago", "Metropolitana", rol);
        // Simular que el usuario ya existe en la base de datos
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        // Llamar al método desactivarUsuario
        usuarioService.desactivarUsuario(1);
        // Verificar que el usuario se haya desactivado
        assertThat(usuario.isUsuarioActivo()).isFalse();
        // Verificar que se haya guardado el usuario desactivado
        verify(usuarioRepository).save(usuario);
    }

    @Test // activarUsuario
    void testActivarUsuario() {
        Rol rol = new Rol(1, "ADMIN", true, Collections.emptyList());
        Usuario usuario = new Usuario(1, "usuario123", "contrasenaSegura", false, "12345678-9", "Juan", "Carlos", "Pérez", "González", "juan.perez@example.com", "Av. Siempre Viva 123", "Santiago", "Metropolitana", rol);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        usuarioService.activarUsuario(1);
        assertThat(usuario.isUsuarioActivo()).isTrue();
        verify(usuarioRepository).save(usuario);
    }
}
