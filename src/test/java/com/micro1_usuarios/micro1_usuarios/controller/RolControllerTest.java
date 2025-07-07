package com.micro1_usuarios.micro1_usuarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro1_usuarios.micro1_usuarios.model.Permiso;
import com.micro1_usuarios.micro1_usuarios.model.Rol;
import com.micro1_usuarios.micro1_usuarios.service.RolService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RolController.class)
public class RolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RolService rolService;

    @Autowired
    private ObjectMapper objectMapper;

    private Rol getRolMock() {
        return new Rol(
                1, "ADMIN", true, List.of(
                        new Permiso(1, "CREAR_USUARIO", true, null),
                        new Permiso(2, "BORRAR_USUARIO", false, null)));
    }

    @Test
    void testCrearRol() throws Exception {
        Rol rol = getRolMock();
        when(rolService.crearRol(any(Rol.class))).thenReturn(rol);

        mockMvc.perform(post("/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rol)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreRol").value("ADMIN"))
                .andExpect(jsonPath("$.estado").value("activo"))
                .andExpect(jsonPath("$.permisos.length()").value(1)); // solo activos
    }

    @Test
    void testListarRoles() throws Exception {
        when(rolService.obtenerRoles()).thenReturn(List.of(getRolMock()));

        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombreRol").value("ADMIN"));
    }

    @Test
    void testListarRolPorId_Encontrado() throws Exception {
        when(rolService.obtenerRolPorId(1)).thenReturn(Optional.of(getRolMock()));

        mockMvc.perform(get("/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreRol").value("ADMIN"));
    }

    @Test
    void testListarRolPorId_NoEncontrado() throws Exception {
        when(rolService.obtenerRolPorId(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/roles/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarRol() throws Exception {
        Rol rolActualizado = getRolMock();
        rolActualizado.setNombreRol("SUPERADMIN");

        when(rolService.actualizarRol(eq(1), any(Rol.class))).thenReturn(rolActualizado);

        mockMvc.perform(put("/roles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rolActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreRol").value("SUPERADMIN"));
    }

    @Test
    void testAsignarPermiso() throws Exception {
        Rol rolConPermiso = getRolMock();

        when(rolService.asignarPermiso(1, 3)).thenReturn(rolConPermiso);

        mockMvc.perform(put("/roles/1/permiso/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreRol").value("ADMIN"));
    }

    @Test
    void testDesactivarRol() throws Exception {
        doNothing().when(rolService).desactivarRol(1);

        mockMvc.perform(put("/roles/1/desactivar"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testActivarRol() throws Exception {
        doNothing().when(rolService).activarRol(1);

        mockMvc.perform(put("/roles/1/activar"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetPermisosActivos_permisosNulo() {
        Rol rol = new Rol();
        rol.setPermisos(null);
        assertThrows(NullPointerException.class, rol::getPermisosActivos);
    }
}
