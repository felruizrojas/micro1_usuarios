package com.micro1_usuarios.micro1_usuarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro1_usuarios.micro1_usuarios.model.Usuario;
import com.micro1_usuarios.micro1_usuarios.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario getUsuarioMock() {
        return new Usuario(
                1, "usuario1", "1234", true, "12345678-9",
                "Juan", "Carlos", "Pérez", "González",
                "juan@email.com", "Calle Falsa 123", "Santiago", "RM", null);
    }

    @Test
    void testCrearUsuario() throws Exception {
        Usuario usuario = getUsuarioMock();
        when(usuarioService.crearUsuario(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user").value("usuario1"))
                .andExpect(jsonPath("$.estado").value("activo"))
                .andExpect(jsonPath("$.pass").doesNotExist()); // por JsonProperty WRITE_ONLY
    }

    @Test
    void testListarUsuarios() throws Exception {
        when(usuarioService.listarUsuarios()).thenReturn(Arrays.asList(getUsuarioMock()));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].user").value("usuario1"));
    }

    @Test
    void testObtenerUsuarioPorId_Encontrado() throws Exception {
        when(usuarioService.listarUsuarioPorId(1)).thenReturn(Optional.of(getUsuarioMock()));

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user").value("usuario1"));
    }

    @Test
    void testObtenerUsuarioPorId_NoEncontrado() throws Exception {
        when(usuarioService.listarUsuarioPorId(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarUsuario() throws Exception {
        Usuario actualizado = getUsuarioMock();
        actualizado.setPrimerNombre("Pedro");

        when(usuarioService.actualizarUsuario(eq(1), any(Usuario.class))).thenReturn(actualizado);

        mockMvc.perform(put("/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.primerNombre").value("Pedro"));
    }

    @Test
    void testAsignarRol() throws Exception {
        Usuario conRol = getUsuarioMock();
        when(usuarioService.asignarRol(1, 2)).thenReturn(conRol);

        mockMvc.perform(put("/usuarios/1/rol/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user").value("usuario1"));
    }

    @Test
    void testDesactivarUsuario() throws Exception {
        doNothing().when(usuarioService).desactivarUsuario(1);

        mockMvc.perform(put("/usuarios/1/desactivar"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testActivarUsuario() throws Exception {
        doNothing().when(usuarioService).activarUsuario(1);

        mockMvc.perform(put("/usuarios/1/activar"))
                .andExpect(status().isNoContent());
    }

    //UsuarioControllerV2.java

    
}
