package com.micro1_usuarios.micro1_usuarios.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import com.micro1_usuarios.micro1_usuarios.model.Permiso;
import com.micro1_usuarios.micro1_usuarios.model.Rol;
import com.micro1_usuarios.micro1_usuarios.repository.PermisoRepository;
import com.micro1_usuarios.micro1_usuarios.repository.RolRepository;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
public class RolServiceTest {

    @InjectMocks
    private RolService rolService;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PermisoRepository permisoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // === Tests para el Modelo Rol ===

    @Test
    void testGetEstadoActivo() {
        Rol rol = new Rol();
        rol.setRolActivo(true);
        assertEquals("activo", rol.getEstado());
    }

    @Test
    void testGetEstadoDesactivo() {
        Rol rol = new Rol();
        rol.setRolActivo(false);
        assertEquals("desactivo", rol.getEstado());
    }

    @Test
    void testGetPermisosActivos_nullList() {
        Rol rol = new Rol();
        rol.setPermisos(null);
        assertNotNull(rol.getPermisosActivos());
        assertTrue(rol.getPermisosActivos().isEmpty());
    }

    @Test
    void testGetPermisosActivos_withActives() {
        Permiso permiso1 = new Permiso();
        permiso1.setPermisoActivo(true);

        Permiso permiso2 = new Permiso();
        permiso2.setPermisoActivo(false);

        Rol rol = new Rol();
        rol.setPermisos(Arrays.asList(permiso1, permiso2));

        var activos = rol.getPermisosActivos();
        assertEquals(1, activos.size());
        assertTrue(activos.contains(permiso1));
        assertFalse(activos.contains(permiso2));
    }

    // === Tests para RolService ===

    @Test
    void testCrearRol_sinPermisos() {
        Rol rol = new Rol();
        rol.setPermisos(null);

        Rol savedRol = new Rol();
        when(rolRepository.save(any(Rol.class))).thenReturn(savedRol);

        Rol result = rolService.crearRol(rol);

        assertNotNull(result);
        verify(rolRepository).save(any(Rol.class));
    }

    @Test
    void testCrearRol_conPermisos() {
        Permiso permiso = new Permiso();
        List<Permiso> permisos = new ArrayList<>();
        permisos.add(permiso);

        Rol rol = new Rol();
        rol.setPermisos(permisos);

        when(rolRepository.save(any())).thenReturn(rol);

        Rol result = rolService.crearRol(rol);

        assertNotNull(result);
        assertEquals(rol, result);
        assertEquals(rol, permiso.getRol());
    }

    @Test
    void testObtenerRoles() {
        List<Rol> lista = List.of(new Rol(), new Rol());
        when(rolRepository.findAll()).thenReturn(lista);

        List<Rol> result = rolService.obtenerRoles();

        assertEquals(2, result.size());
    }

    @Test
    void testObtenerRolPorId_existente() {
        Rol rol = new Rol();
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));

        Optional<Rol> result = rolService.obtenerRolPorId(1);
        assertTrue(result.isPresent());
    }

    @Test
    void testActualizarRol_existente() {
        Rol existente = new Rol(1, "Admin", true, null);
        Rol nuevo = new Rol(0, "SuperAdmin", true, null);

        when(rolRepository.findById(1)).thenReturn(Optional.of(existente));
        when(rolRepository.save(any())).thenReturn(existente);

        Rol actualizado = rolService.actualizarRol(1, nuevo);

        assertEquals("SuperAdmin", actualizado.getNombreRol());
    }

    @Test
    void testActualizarRol_noExistente() {
        Rol nuevo = new Rol(0, "User", true, null);

        when(rolRepository.findById(1)).thenReturn(Optional.empty());
        when(rolRepository.save(any())).thenReturn(nuevo);

        Rol result = rolService.actualizarRol(1, nuevo);

        assertEquals(1, result.getId());
        assertTrue(result.isRolActivo());
    }

    @Test
    void testAsignarPermiso_correctamente() {
        Rol rol = new Rol();
        rol.setId(1);
        Permiso permiso = new Permiso();
        permiso.setId(2);
        permiso.setRol(null);

        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        when(permisoRepository.findById(2)).thenReturn(Optional.of(permiso));

        Rol result = rolService.asignarPermiso(1, 2);

        assertEquals(rol, permiso.getRol());
        verify(permisoRepository).save(permiso);
        assertEquals(rol, result);
    }

    @Test
    void testAsignarPermiso_yaAsignado() {
        Rol rol = new Rol();
        rol.setId(1);
        Permiso permiso = new Permiso();
        permiso.setId(2);
        permiso.setRol(rol);

        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        when(permisoRepository.findById(2)).thenReturn(Optional.of(permiso));

        Rol result = rolService.asignarPermiso(1, 2);

        verify(permisoRepository, never()).save(permiso);
        assertEquals(rol, result);
    }

    @Test
    void testAsignarPermiso_rolNoEncontrado() {
        when(rolRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> rolService.asignarPermiso(1, 2));
    }

    @Test
    void testAsignarPermiso_permisoNoEncontrado() {
        when(rolRepository.findById(1)).thenReturn(Optional.of(new Rol()));
        when(permisoRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> rolService.asignarPermiso(1, 2));
    }

    @Test
    void testDesactivarRol() {
        Rol rol = new Rol();
        rol.setRolActivo(true);

        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        rolService.desactivarRol(1);

        assertFalse(rol.isRolActivo());
        verify(rolRepository).save(rol);
    }

    @Test
    void testActivarRol() {
        Rol rol = new Rol();
        rol.setRolActivo(false);

        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        rolService.activarRol(1);

        assertTrue(rol.isRolActivo());
        verify(rolRepository).save(rol);
    }

    @Test
    void testActivarRol_noEncontrado() {
        when(rolRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> rolService.activarRol(1));
    }

    @Test
    void testDesactivarRol_noEncontrado() {
        when(rolRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> rolService.desactivarRol(1));
    }
}
