package com.micro1_usuarios.micro1_usuarios.service;

import com.micro1_usuarios.micro1_usuarios.model.Permiso;
import com.micro1_usuarios.micro1_usuarios.model.Rol;
import com.micro1_usuarios.micro1_usuarios.repository.PermisoRepository;
import com.micro1_usuarios.micro1_usuarios.repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
        var resultado = rolService.listarRoles();
        assertThat(resultado).hasSize(2).contains(r1, r2);
        verify(rolRepository).findAll();
    }

    @Test // test get --> listar rol por id
    void testListarRolPorId() {
        Rol rol = new Rol(1, "ADMIN", true, Collections.emptyList());
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        Optional<Rol> resultado = rolService.listarRolPorId(1);
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
}
