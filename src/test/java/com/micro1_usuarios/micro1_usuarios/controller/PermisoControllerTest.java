package com.micro1_usuarios.micro1_usuarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro1_usuarios.micro1_usuarios.model.Permiso;
import com.micro1_usuarios.micro1_usuarios.service.PermisoService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PermisoController.class)
public class PermisoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PermisoService permisoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Permiso getPermisoMock() {
        return new Permiso(1, "GESTIONAR_USUARIOS", true, null);
    }

    @Test
    void testCrearPermiso() throws Exception {
        Permiso permiso = getPermisoMock();
        when(permisoService.crearPermiso(any(Permiso.class))).thenReturn(permiso);

        mockMvc.perform(post("/permisos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(permiso)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombrePermiso").value("GESTIONAR_USUARIOS"))
                .andExpect(jsonPath("$.estado").value("activo"))
                .andExpect(jsonPath("$.permisoActivo").doesNotExist()); // no serializado
    }

    @Test
    void testListarPermisos() throws Exception {
        when(permisoService.listarPermisos()).thenReturn(List.of(getPermisoMock()));

        mockMvc.perform(get("/permisos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombrePermiso").value("GESTIONAR_USUARIOS"));
    }

    @Test
    void testObtenerPermisoPorId_Encontrado() throws Exception {
        when(permisoService.listarPermisoPorId(1)).thenReturn(getPermisoMock());

        mockMvc.perform(get("/permisos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("activo"));
    }

    @Test
    void testObtenerPermisoPorId_NoEncontrado() throws Exception {
        when(permisoService.listarPermisoPorId(99)).thenReturn(null);

        mockMvc.perform(get("/permisos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarPermiso() throws Exception {
        Permiso actualizado = getPermisoMock();
        actualizado.setNombrePermiso("MODIFICAR_DATOS");

        when(permisoService.actualizarPermiso(eq(1), any(Permiso.class))).thenReturn(actualizado);

        mockMvc.perform(put("/permisos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombrePermiso").value("MODIFICAR_DATOS"));
    }

    @Test
    void testDesactivarPermiso() throws Exception {
        doNothing().when(permisoService).desactivarPermiso(1);
        mockMvc.perform(put("/permisos/1/desactivar"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testActivarPermiso() throws Exception {
        doNothing().when(permisoService).activarPermiso(1);
        mockMvc.perform(put("/permisos/1/activar"))
                .andExpect(status().isNoContent());
    }
}
