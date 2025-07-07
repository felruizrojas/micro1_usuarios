package com.micro1_usuarios.micro1_usuarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro1_usuarios.micro1_usuarios.assemblers.RolModelAssembler;
import com.micro1_usuarios.micro1_usuarios.model.Rol;
import com.micro1_usuarios.micro1_usuarios.service.RolService;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RolControllerV2.class)
public class RolControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RolService rolService;

    @MockBean
    private RolModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Rol getRolMock() {
        Rol rol = new Rol();
        rol.setId(1);
        rol.setNombreRol("Administrador");
        return rol;
    }

    @Test
    void testCrearRol() throws Exception {
        Rol rol = getRolMock();

        when(rolService.crearRol(any(Rol.class))).thenReturn(rol);
        when(assembler.toModel(any(Rol.class))).thenReturn(EntityModel.of(rol));

        mockMvc.perform(post("/api/v2/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rol)))
                .andExpect(status().isCreated());
    }

    @Test
    void testListarRoles() throws Exception {
        Rol rol = getRolMock();
        when(rolService.obtenerRoles()).thenReturn(List.of(rol));
        when(assembler.toModel(any(Rol.class))).thenReturn(EntityModel.of(rol));

        mockMvc.perform(get("/api/v2/roles"))
                .andExpect(status().isOk());
    }

    @Test
    void testObtenerRolPorId_Encontrado() throws Exception {
        Rol rol = getRolMock();
        when(rolService.obtenerRolPorId(1)).thenReturn(Optional.of(rol));
        when(assembler.toModel(rol)).thenReturn(EntityModel.of(rol));

        mockMvc.perform(get("/api/v2/roles/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testObtenerRolPorId_NoEncontrado() throws Exception {
        when(rolService.obtenerRolPorId(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v2/roles/1"))
                .andExpect(status().isNotFound()); // cambiar si usas ResponseStatusException
    }

    @Test
    void testActualizarRol() throws Exception {
        Rol rol = getRolMock();

        when(rolService.actualizarRol(eq(1), any(Rol.class))).thenReturn(rol);
        when(assembler.toModel(rol)).thenReturn(EntityModel.of(rol));

        mockMvc.perform(put("/api/v2/roles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rol)))
                .andExpect(status().isOk());
    }

    @Test
    void testAsignarPermiso() throws Exception {
        Rol rol = getRolMock();

        when(rolService.asignarPermiso(1, 2)).thenReturn(rol);
        when(assembler.toModel(rol)).thenReturn(EntityModel.of(rol));

        mockMvc.perform(put("/api/v2/roles/1/permiso/2"))
                .andExpect(status().isOk());
    }

    @Test
    void testDesactivarRol() throws Exception {
        doNothing().when(rolService).desactivarRol(1);

        mockMvc.perform(put("/api/v2/roles/1/desactivar"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testActivarRol() throws Exception {
        doNothing().when(rolService).activarRol(1);

        mockMvc.perform(put("/api/v2/roles/1/activar"))
                .andExpect(status().isNoContent());
    }

    // HATEOAS
    @Test
    public void testToModel() {
        Rol rol = new Rol();
        rol.setId(42);

        RolModelAssembler assembler = new RolModelAssembler();
        EntityModel<Rol> model = assembler.toModel(rol);

        assertEquals(rol, model.getContent());

        // Verificar que existan los links esperados
        assertTrue(model.hasLink("self"));
        assertTrue(model.hasLink("roles"));
        assertTrue(model.hasLink("asignarPermiso"));
        assertTrue(model.hasLink("desactivar"));
        assertTrue(model.hasLink("activar"));

        // Verificar que el link self contenga el id correcto
        String selfHref = model.getRequiredLink("self").getHref();
        assertTrue(selfHref.contains("/" + rol.getId()));
    }
}
