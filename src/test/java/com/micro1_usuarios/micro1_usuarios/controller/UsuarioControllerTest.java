/*
package com.micro1_usuarios.micro1_usuarios.controller;

import com.micro1_usuarios.micro1_usuarios.model.Usuario;
import com.micro1_usuarios.micro1_usuarios.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @Test
    void testCrearUsuario() {
        Usuario input = new Usuario();
        input.setNombre("Juan");

        Usuario saved = new Usuario();
        saved.setId(1);
        saved.setNombre("Juan");

        when(usuarioService.crearUsuario(input)).thenReturn(saved);

        ResponseEntity<Usuario> response = usuarioController.crearUsuario(input);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Juan", response.getBody().getNombre());
        verify(usuarioService).crearUsuario(input);
    }

    @Test
    void testListarUsuarios() {
        List<Usuario> usuarios = Arrays.asList(
            new Usuario(1, "Juan"),
            new Usuario(2, "Ana")
        );

        when(usuarioService.listarUsuarios()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioController.listarUsuarios();

        assertEquals(2, resultado.size());
        verify(usuarioService).listarUsuarios();
    }

    @Test
    void testObtenerUsuarioPorId_Existente() {
        Usuario usuario = new Usuario(1, "Juan");
        when(usuarioService.listarUsuarioPorId(1)).thenReturn(Optional.of(usuario));

        ResponseEntity<Usuario> response = usuarioController.obtenerUsuarioPorId(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Juan", response.getBody().getNombre());
    }

    @Test
    void testObtenerUsuarioPorId_NoExistente() {
        when(usuarioService.listarUsuarioPorId(1)).thenReturn(Optional.empty());

        ResponseEntity<Usuario> response = usuarioController.obtenerUsuarioPorId(1);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testActualizarUsuario() {
        Usuario updated = new Usuario(1, "Pedro");
        when(usuarioService.actualizarUsuario(eq(1), any())).thenReturn(updated);

        ResponseEntity<Usuario> response = usuarioController.actualizarUsuario(1, updated);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Pedro", response.getBody().getNombre());
    }

    @Test
    void testAsignarRol() {
        Usuario usuarioConRol = new Usuario(1, "Juan");
        // Supongamos que tiene un rol ahora
        when(usuarioService.asignarRol(1, 2)).thenReturn(usuarioConRol);

        ResponseEntity<Usuario> response = usuarioController.asignarRol(1, 2);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Juan", response.getBody().getNombre());
    }

    @Test
    void testDesactivarUsuario() {
        doNothing().when(usuarioService).desactivarUsuario(1);

        ResponseEntity<Void> response = usuarioController.desactivarUsuario(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(usuarioService).desactivarUsuario(1);
    }

    @Test
    void testActivarUsuario() {
        doNothing().when(usuarioService).activarUsuario(1);

        ResponseEntity<Void> response = usuarioController.activarUsuario(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(usuarioService).activarUsuario(1);
    }
}

*/