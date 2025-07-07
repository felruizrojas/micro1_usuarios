package com.micro1_usuarios.micro1_usuarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro1_usuarios.micro1_usuarios.assemblers.PermisoModelAssembler;
import com.micro1_usuarios.micro1_usuarios.model.Permiso;
import com.micro1_usuarios.micro1_usuarios.service.PermisoService;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PermisoControllerV2.class)
public class PermisoControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PermisoService permisoService;

    @MockBean
    private PermisoModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Permiso getPermisoMock() {
        Permiso permiso = new Permiso();
        permiso.setId(1);
        permiso.setNombrePermiso("VER_USUARIOS");
        return permiso;
    }

    @Test
    void testCrearPermiso() throws Exception {
        Permiso permiso = getPermisoMock();

        when(permisoService.crearPermiso(any(Permiso.class))).thenReturn(permiso);
        when(assembler.toModel(any(Permiso.class))).thenReturn(EntityModel.of(permiso));

        mockMvc.perform(post("/api/v2/permisos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(permiso)))
                .andExpect(status().isCreated());
    }

    @Test
    void testListarPermisos() throws Exception {
        Permiso permiso = getPermisoMock();
        when(permisoService.listarPermisos()).thenReturn(List.of(permiso));
        when(assembler.toModel(permiso)).thenReturn(EntityModel.of(permiso));

        mockMvc.perform(get("/api/v2/permisos"))
                .andExpect(status().isOk());
    }

    @Test
    void testObtenerPermisoPorId() throws Exception {
        Permiso permiso = getPermisoMock();
        when(permisoService.listarPermisoPorId(1)).thenReturn(permiso);
        when(assembler.toModel(permiso)).thenReturn(EntityModel.of(permiso));

        mockMvc.perform(get("/api/v2/permisos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testActualizarPermiso() throws Exception {
        Permiso permiso = getPermisoMock();

        when(permisoService.actualizarPermiso(eq(1), any(Permiso.class))).thenReturn(permiso);
        when(assembler.toModel(permiso)).thenReturn(EntityModel.of(permiso));

        mockMvc.perform(put("/api/v2/permisos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(permiso)))
                .andExpect(status().isOk());
    }

    @Test
    void testDesactivarPermiso() throws Exception {
        doNothing().when(permisoService).desactivarPermiso(1);

        mockMvc.perform(put("/api/v2/permisos/1/desactivar"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testActivarPermiso() throws Exception {
        doNothing().when(permisoService).activarPermiso(1);

        mockMvc.perform(put("/api/v2/permisos/1/activar"))
                .andExpect(status().isNoContent());
    }

    // HATEOAS

    @Test
    public void testToModel() {
        Permiso permiso = new Permiso();
        permiso.setId(10);

        PermisoModelAssembler assembler = new PermisoModelAssembler();
        EntityModel<Permiso> model = assembler.toModel(permiso);

        // Validar que el contenido sea el permiso esperado
        assertEquals(permiso, model.getContent());

        // Validar que existen los links esperados
        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("permisos"));
        assertTrue(model.hasLink("desactivar"));
        assertTrue(model.hasLink("activar"));

        // Validar que el link self contiene el id correcto
        String selfHref = model.getRequiredLink("self").getHref();
        assertTrue(selfHref.contains("/" + permiso.getId()));
    }
}
