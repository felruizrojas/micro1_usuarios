package com.micro1_usuarios.micro1_usuarios.service;

import com.micro1_usuarios.micro1_usuarios.model.Permiso;
import com.micro1_usuarios.micro1_usuarios.model.Rol;
import com.micro1_usuarios.micro1_usuarios.repository.PermisoRepository;
import com.micro1_usuarios.micro1_usuarios.repository.RolRepository;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PermisoRepository permisoRepository;

    @InjectMocks
    private RolService rolService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test // test post --> crear rol
    void testCrearRol() {
        Rol rol = new Rol(1, "ADMIN", true, Collections.emptyList());
        when(rolRepository.save(any(Rol.class))).thenReturn(rol);
        Rol resultado = rolService.crearRol(rol);
        assertThat(resultado.getId()).isEqualTo(1);
        assertThat(resultado.getNombreRol()).isEqualTo("ADMIN");
        assertThat(resultado.isRolActivo()).isTrue();
        assertThat(resultado.getPermisos()).isEmpty();
        verify(rolRepository).save(rol);
    }

    @Test // test get --> listar roles
    void testListarRoles() {
        Rol r1 = new Rol(1, "ADMIN", true, Collections.emptyList());
        Rol r2 = new Rol(2, "USER", true, Collections.emptyList());
        when(rolRepository.findAll()).thenReturn(Arrays.asList(r1, r2));
        var resultado = rolService.obtenerRoles();
        assertThat(resultado).hasSize(2).contains(r1, r2);
        verify(rolRepository).findAll();
    }

    @Test // test get --> listar rol por id
    void testListarRolPorId() {
        Rol rol = new Rol(1, "ADMIN", true, Collections.emptyList());
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        Optional<Rol> resultado = rolService.obtenerRolPorId(1);
        assertThat(resultado).isPresent().contains(rol);
        verify(rolRepository).findById(1);
    }

    @Test // test put --> actualizar rol existente
    void testActualizarRolExistente() {
        Rol existente = new Rol(1, "ADMIN", true, Collections.emptyList());
        Rol actualizado = new Rol(1, "SUPERADMIN", true, Collections.emptyList());
        when(rolRepository.findById(1)).thenReturn(Optional.of(existente));
        when(rolRepository.save(any(Rol.class))).thenReturn(actualizado);
        Rol resultado = rolService.actualizarRol(1, actualizado);
        assertThat(resultado.getNombreRol()).isEqualTo("SUPERADMIN");
        verify(rolRepository).save(existente);
    }

    @Test // test put --> asignar permiso
    void testAsignarPermiso() {
        Rol rol = new Rol(1, "ADMIN", true, Collections.emptyList());
        Permiso permiso = new Permiso(1, "VER_USUARIOS", true, null);
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        when(permisoRepository.findById(1)).thenReturn(Optional.of(permiso));
        when(permisoRepository.save(any(Permiso.class))).thenReturn(permiso);
        Rol resultado = rolService.asignarPermiso(1, 1);
        assertThat(resultado).isEqualTo(rol);
        assertThat(permiso.getRol()).isEqualTo(rol);
        verify(permisoRepository).save(permiso);
    }

    @Test // test desactivar rol
    void testDesactivarRol() {
        Rol rol = new Rol(1, "ADMIN", true, Collections.emptyList());
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        rolService.desactivarRol(1);
        assertThat(rol.isRolActivo()).isFalse();
        verify(rolRepository).save(rol);
    }

    @Test // test activar rol
    void testActivarRol() {
        Rol rol = new Rol(1, "ADMIN", false, Collections.emptyList());
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        rolService.activarRol(1);
        assertThat(rol.isRolActivo()).isTrue();
        verify(rolRepository).save(rol);
    }

    /*
     *
     * 
     * 
     * TEST FALTANTES
     * 
     * 
     * 
     */

    @Test
    void testCrearRolConPermisos() {
        Rol rol = new Rol(1, "ADMIN", true, null);
        Permiso permiso = new Permiso(1, "CREAR_USUARIOS", true, null);
        rol.setPermisos(Arrays.asList(permiso));
        when(rolRepository.save(any(Rol.class))).thenReturn(rol);
        Rol resultado = rolService.crearRol(rol);
        assertThat(resultado.getPermisos()).hasSize(1);
        assertThat(permiso.getRol()).isEqualTo(rol);
        verify(rolRepository).save(rol);
    }

    @Test
    void testActualizarRolNoExistente() {
        Rol nuevoRol = new Rol(0, "NEW_ROLE", true, Collections.emptyList());
        when(rolRepository.findById(99)).thenReturn(Optional.empty());
        when(rolRepository.save(any(Rol.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Rol resultado = rolService.actualizarRol(99, nuevoRol);
        assertThat(resultado.getId()).isEqualTo(99);
        assertThat(resultado.getNombreRol()).isEqualTo("NEW_ROLE");
        assertThat(resultado.isRolActivo()).isTrue();
        verify(rolRepository).save(nuevoRol);
    }

    @Test
    void testAsignarPermiso_RolNoEncontrado() {
        when(rolRepository.findById(1)).thenReturn(Optional.empty());
        EntityNotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> rolService.asignarPermiso(1, 1));
        assertThat(exception.getMessage()).contains("Rol no encontrado con ID: 1");
        verify(rolRepository).findById(1);
    }

    @Test
    void testAsignarPermiso_PermisoNoEncontrado() {
        Rol rol = new Rol(1, "ADMIN", true, Collections.emptyList());
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        when(permisoRepository.findById(1)).thenReturn(Optional.empty());
        EntityNotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> rolService.asignarPermiso(1, 1));
        assertThat(exception.getMessage()).contains("Permiso no encontrado con ID: 1");
        verify(rolRepository).findById(1);
        verify(permisoRepository).findById(1);
    }

    @Test
    void testObtenerRolPorIdNoExistente() {
        when(rolRepository.findById(999)).thenReturn(Optional.empty());
        Optional<Rol> resultado = rolService.obtenerRolPorId(999);
        assertThat(resultado).isNotPresent();
        verify(rolRepository).findById(999);
    }

    @Test
    void testAsignarPermisoYaAsignado() {
        Rol rol = new Rol(1, "ADMIN", true, Collections.emptyList());
        Permiso permiso = new Permiso(1, "EDITAR", true, rol); // ya tiene el rol
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        when(permisoRepository.findById(1)).thenReturn(Optional.of(permiso));
        Rol resultado = rolService.asignarPermiso(1, 1);
        // No se debería guardar nuevamente si ya está asignado
        verify(permisoRepository, never()).save(any());
        assertThat(resultado).isEqualTo(rol);
    }

    @Test
    void testActualizarRolSinCambiarNombre() {
        Rol existente = new Rol(1, "ADMIN", true, Collections.emptyList());
        Rol nuevo = new Rol(1, null, true, Collections.emptyList());
        when(rolRepository.findById(1)).thenReturn(Optional.of(existente));
        when(rolRepository.save(any(Rol.class))).thenReturn(existente);
        Rol resultado = rolService.actualizarRol(1, nuevo);
        assertThat(resultado.getNombreRol()).isEqualTo("ADMIN");
        verify(rolRepository).save(existente);
    }

    @Test
    void testDesactivarRolNoExistente() {
        when(rolRepository.findById(999)).thenReturn(Optional.empty());
        EntityNotFoundException ex = org.junit.jupiter.api.Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> rolService.desactivarRol(999));
        assertThat(ex.getMessage()).contains("Rol no encontrado con ID: 999");
        verify(rolRepository).findById(999);
    }

    @Test
    void testActivarRolNoExistente() {
        when(rolRepository.findById(999)).thenReturn(Optional.empty());
        EntityNotFoundException ex = org.junit.jupiter.api.Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> rolService.activarRol(999));
        assertThat(ex.getMessage()).contains("Rol no encontrado con ID: 999");
        verify(rolRepository).findById(999);
    }

    @Test
    void testAsignarPermisoConPermisoRolNull() {
        Rol rol = new Rol(1, "ADMIN", true, Collections.emptyList());
        Permiso permiso = new Permiso(1, "VER_USUARIOS", true, null); // permiso.getRol() == null
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        when(permisoRepository.findById(1)).thenReturn(Optional.of(permiso));
        when(permisoRepository.save(any(Permiso.class))).thenReturn(permiso);
        Rol resultado = rolService.asignarPermiso(1, 1);
        assertThat(resultado).isEqualTo(rol);
        assertThat(permiso.getRol()).isEqualTo(rol);
        verify(permisoRepository).save(permiso);
    }

    /////////////////////
    ///
    ///

    @Test
    public void testGetEstado_activo() {
        Rol rol = new Rol();
        rol.setRolActivo(true);
        assertEquals("activo", rol.getEstado());
    }

    @Test
    public void testGetEstado_desactivo() {
        Rol rol = new Rol();
        rol.setRolActivo(false);
        assertEquals("desactivo", rol.getEstado());
    }

    @Test
    public void testGetPermisosActivos_filtrado() {
        Rol rol = new Rol();
        rol.setPermisos(new ArrayList<>());

        Permiso permisoActivo = new Permiso();
        permisoActivo.setPermisoActivo(true);

        Permiso permisoInactivo = new Permiso();
        permisoInactivo.setPermisoActivo(false);

        rol.getPermisos().add(permisoActivo);
        rol.getPermisos().add(permisoInactivo);

        List<Permiso> permisosActivos = rol.getPermisosActivos();
        assertEquals(1, permisosActivos.size());
        assertTrue(permisosActivos.contains(permisoActivo));
        assertFalse(permisosActivos.contains(permisoInactivo));
    }

    @Test
    public void testGetPermisosActivos_permisosNulo() {
        Rol rol = new Rol();
        rol.setPermisos(null);
        // Esto puede lanzar NullPointerException si no manejas null en el método.
        // Si el método no maneja null, este test sirve para que veas que tienes que
        // modificar el método
        assertThrows(NullPointerException.class, rol::getPermisosActivos);
    }
}
