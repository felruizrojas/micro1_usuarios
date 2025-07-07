package com.micro1_usuarios.micro1_usuarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro1_usuarios.micro1_usuarios.assemblers.UsuarioModelAssembler;
import com.micro1_usuarios.micro1_usuarios.model.Usuario;
import com.micro1_usuarios.micro1_usuarios.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioControllerV2.class)
public class UsuarioControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCrearUsuario() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setPrimerNombre("Felipe");

        when(usuarioService.crearUsuario(any(Usuario.class))).thenReturn(usuario);
        when(assembler.toModel(any(Usuario.class))).thenReturn(EntityModel.of(usuario));

        mockMvc.perform(post("/api/v2/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated());
    }

    @Test
    void testListarUsuarios() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setPrimerNombre("Felipe");

        List<Usuario> usuarios = List.of(usuario);

        when(usuarioService.listarUsuarios()).thenReturn(usuarios);
        when(assembler.toModel(any(Usuario.class))).thenReturn(EntityModel.of(usuario));

        mockMvc.perform(get("/api/v2/usuarios"))
                .andExpect(status().isOk());
    }

    @Test
    void testObtenerUsuarioPorId_Success() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setPrimerNombre("Felipe");

        when(usuarioService.listarUsuarioPorId(1)).thenReturn(Optional.of(usuario));
        when(assembler.toModel(usuario)).thenReturn(EntityModel.of(usuario));

        mockMvc.perform(get("/api/v2/usuarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testObtenerUsuarioPorId_NotFound() throws Exception {
        when(usuarioService.listarUsuarioPorId(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v2/usuarios/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarUsuario() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setPrimerNombre("Actualizado");

        when(usuarioService.actualizarUsuario(eq(1), any(Usuario.class))).thenReturn(usuario);
        when(assembler.toModel(usuario)).thenReturn(EntityModel.of(usuario));

        mockMvc.perform(put("/api/v2/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk());
    }

    @Test
    void testAsignarRol() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1);

        when(usuarioService.asignarRol(1, 2)).thenReturn(usuario);
        when(assembler.toModel(usuario)).thenReturn(EntityModel.of(usuario));

        mockMvc.perform(put("/api/v2/usuarios/1/rol/2"))
                .andExpect(status().isOk());
    }

    @Test
    void testDesactivarUsuario() throws Exception {
        Mockito.doNothing().when(usuarioService).desactivarUsuario(1);

        mockMvc.perform(put("/api/v2/usuarios/1/desactivar"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testActivarUsuario() throws Exception {
        Mockito.doNothing().when(usuarioService).activarUsuario(1);

        mockMvc.perform(put("/api/v2/usuarios/1/activar"))
                .andExpect(status().isNoContent());
    }

    // HATEOAS
    @Test
    public void testToModel() {
        Usuario usuario = new Usuario();
        usuario.setId(123);

        UsuarioModelAssembler assembler = new UsuarioModelAssembler();
        EntityModel<Usuario> model = assembler.toModel(usuario);

        // Verificar que el modelo contiene el usuario
        assertEquals(usuario, model.getContent());

        // Verificar links
        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("listarTodos"));
        assertTrue(model.hasLink("crearUsuario"));
        assertTrue(model.hasLink("actualizarUsuario"));
        assertTrue(model.hasLink("asignarRol"));
        assertTrue(model.hasLink("desactivarUsuario"));
        assertTrue(model.hasLink("activarUsuario"));

        // Verificar que el link self contiene el id correcto
        String selfHref = model.getRequiredLink("self").getHref();
        assertTrue(selfHref.contains("/" + usuario.getId()));
    }

}
