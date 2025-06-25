package com.micro1_usuarios.micro1_usuarios.service;

import com.micro1_usuarios.micro1_usuarios.model.Permiso;
import com.micro1_usuarios.micro1_usuarios.repository.PermisoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class PermisoServiceTest {

    @Mock
    private PermisoRepository permisoRepository;

    @InjectMocks
    private PermisoService permisoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test // test post --> crear permiso
    void testCrearPermiso() {
        Permiso permiso = new Permiso(
                0,
                "Otorgar permisos",
                true,
                null);
        Permiso permisoCreado = new Permiso(
                1,
                "Otorgar permisos",
                true,
                null);
        when(permisoRepository.save(permiso)).thenReturn(permisoCreado);
        Permiso resultado = permisoService.crearPermiso(permiso);
        assertThat(resultado.getId()).isEqualTo(1);
        assertThat(resultado.getNombrePermiso()).isEqualTo("Otorgar permisos");
        assertThat(resultado.isPermisoActivo()).isTrue();
        verify(permisoRepository).save(permiso);
    }

    @Test // test get --> listar permisos
    void testListarPermisos() {
        Permiso p1 = new Permiso(1, "Editar usuarios", true, null);
        Permiso p2 = new Permiso(2, "Editar inventario", true, null);
        when(permisoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));
        List<Permiso> resultado = permisoService.listarPermisos();
        assertThat(resultado).hasSize(2).contains(p1, p2);
        verify(permisoRepository).findAll();
    }

    @Test // test get --> listar permiso por id
    void testListarPermisoPorId() {
        Permiso permiso = new Permiso(1, "Editar usuarios", true, null);
        when(permisoRepository.findById(1)).thenReturn(Optional.of(permiso));
        Optional<Permiso> resultado = permisoService.listarPermisoPorId(1);
        assertThat(resultado).isPresent().isEqualTo(Optional.of(permiso));
        assertThat(resultado.get().getId()).isEqualTo(1);
        assertThat(resultado.get().getNombrePermiso()).isEqualTo("Editar usuarios");
        assertThat(resultado.get().isPermisoActivo()).isTrue();
        assertThat(resultado.get().getRol()).isNull();
        verify(permisoRepository).findById(1);
    }

    @Test // test put --> actualizar permiso existente
    void testActualizarPermisoExistente() {
        Permiso existente = new Permiso(1, "Editar usuarios", true, null);
        Permiso actualizado = new Permiso(1, "Cambiar roles de usuarios", true, null);
        when(permisoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(permisoRepository.save(existente)).thenReturn(existente);
        Permiso resultado = permisoService.actualizarPermiso(1, actualizado);
        assertEquals("Cambiar roles de usuarios", resultado.getNombrePermiso());
        assertEquals(1, resultado.getId());
        assertTrue(resultado.isPermisoActivo());
        verify(permisoRepository).findById(1);
        verify(permisoRepository).save(existente);
    }

    @Test // test desactivar permiso
    void testDesactivarPermiso() {
        Permiso permiso = new Permiso(1, "Cambiar roles de usuarios", true, null);
        when(permisoRepository.findById(1)).thenReturn(Optional.of(permiso));
        permisoService.desactivarPermiso(1);
        assertThat(permiso.isPermisoActivo()).isFalse();
        verify(permisoRepository).save(permiso);
    }

    @Test // test activar permiso
    void testActivarPermiso() {
        Permiso permiso = new Permiso(1, "Cambiar roles de usuarios", false, null);
        when(permisoRepository.findById(1)).thenReturn(Optional.of(permiso));
        permisoService.activarPermiso(1);
        assertThat(permiso.isPermisoActivo()).isTrue();
        verify(permisoRepository).save(permiso);
    }
}
