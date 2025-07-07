package com.micro1_usuarios.micro1_usuarios.service;

import com.micro1_usuarios.micro1_usuarios.model.Rol;
import com.micro1_usuarios.micro1_usuarios.model.Usuario;
import com.micro1_usuarios.micro1_usuarios.repository.RolRepository;
import com.micro1_usuarios.micro1_usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test // test post --> crear usuario
    void testCrearUsuario() {
        Rol rol = new Rol(
                1, // id
                "ADMIN", // nombreRol
                true, // rolActivo
                Collections.emptyList() // permisos (lista vacía en esta prueba)
        );
        Usuario usuario = new Usuario(
                0,
                "usuario123", // user
                "contrasenaSegura", // pass
                true, // usuarioActivo
                "12345678-9", // run
                "Juan", // primerNombre
                "Carlos", // segundoNombre
                "Pérez", // primerApellido
                "González", // segundoApellido
                "juan.perez@example.com", // correo
                "Av. Siempre Viva 123", // direccion
                "Santiago", // ciudad
                "Metropolitana", // region
                rol // rol
        );
        Usuario usuarioCreado = new Usuario(
                1, // id asignado por la BD al guardar
                "usuario123", //
                "contrasenaSegura", //
                true, //
                "12345678-9", //
                "Juan", //
                "Carlos", //
                "Pérez", //
                "González", //
                "juan.perez@example.com", //
                "Av. Siempre Viva 123", //
                "Santiago", //
                "Metropolitana", //
                rol); //
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        when(usuarioRepository.save(usuario)).thenReturn(usuarioCreado);
        Usuario resultado = usuarioService.crearUsuario(usuario);
        assertThat(resultado.getId()).isEqualTo(1);
        assertThat(resultado.getUser()).isEqualTo("usuario123");
        assertThat(resultado.getPass()).isEqualTo("contrasenaSegura");
        assertThat(resultado.isUsuarioActivo()).isTrue();
        assertThat(resultado.getRun()).isEqualTo("12345678-9");
        assertThat(resultado.getPrimerNombre()).isEqualTo("Juan");
        assertThat(resultado.getSegundoNombre()).isEqualTo("Carlos");
        assertThat(resultado.getPrimerApellido()).isEqualTo("Pérez");
        assertThat(resultado.getSegundoApellido()).isEqualTo("González");
        assertThat(resultado.getCorreo()).isEqualTo("juan.perez@example.com");
        assertThat(resultado.getDireccion()).isEqualTo("Av. Siempre Viva 123");
        assertThat(resultado.getCiudad()).isEqualTo("Santiago");
        assertThat(resultado.getRegion()).isEqualTo("Metropolitana");
        assertThat(resultado.getRol().getNombreRol()).isEqualTo("ADMIN");
        verify(usuarioRepository).save(usuario);
    }

    @Test // test get --> listar usuarios
    void testListarUsuarios() {
        Rol rol = new Rol(1, "ADMIN", true, Collections.emptyList());
        Usuario u1 = new Usuario(1, "usuario123", "contrasenaSegura", true, "12345678-9", "Juan", "Carlos", "Pérez",
                "González", "juan.perez@example.com", "Av. Siempre Viva 123", "Santiago", "Metropolitana", rol);
        Usuario u2 = new Usuario(2, "usuario456", "otraContrasena", true, "98765432-1", "Ana", "Maria", "López",
                "Fernández", "ana.lopez@example.com", "Av. Siempre Viva 456", "Santiago", "Metropolitana", rol);
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(u1, u2));
        List<Usuario> resultado = usuarioService.listarUsuarios();
        assertThat(resultado).hasSize(2).contains(u1, u2);
        verify(usuarioRepository).findAll();
    }

    @Test // test get --> listar usuario por id
    void testListarUsuarioPorId() {
        Rol rol = new Rol(1, "ADMIN", true, Collections.emptyList());
        Usuario u = new Usuario(1, "usuario123", "contrasenaSegura", true, "12345678-9", "Juan", "Carlos", "Pérez",
                "González", "juan.perez@example.com", "Av. Siempre Viva 123", "Santiago", "Metropolitana", rol);
        // Simular que el usuario existe en la base de datos
        // y que el método findById devuelve un Optional con el usuario.
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(u));
        // Llamar al método listarUsuarioPorId
        Optional<Usuario> resultado = usuarioService.listarUsuarioPorId(1);
        // Verificar que el resultado es un Optional con el usuario esperado
        assertThat(resultado).isPresent().isEqualTo(Optional.of(u));
        // Verificar que se haya llamado al método findById del repositorio
        assertThat(resultado.get().getId()).isEqualTo(1);
        assertThat(resultado.get().getUser()).isEqualTo("usuario123");
        assertThat(resultado.get().getPass()).isEqualTo("contrasenaSegura");
        assertThat(resultado.get().isUsuarioActivo()).isTrue();
        assertThat(resultado.get().getRun()).isEqualTo("12345678-9");
        assertThat(resultado.get().getPrimerNombre()).isEqualTo("Juan");
        assertThat(resultado.get().getSegundoNombre()).isEqualTo("Carlos");
        assertThat(resultado.get().getPrimerApellido()).isEqualTo("Pérez");
        assertThat(resultado.get().getSegundoApellido()).isEqualTo("González");
        assertThat(resultado.get().getCorreo()).isEqualTo("juan.perez@example.com");
        assertThat(resultado.get().getDireccion()).isEqualTo("Av. Siempre Viva 123");
        assertThat(resultado.get().getCiudad()).isEqualTo("Santiago");
        assertThat(resultado.get().getRegion()).isEqualTo("Metropolitana");
        assertThat(resultado.get().getRol().getNombreRol()).isEqualTo("ADMIN");
        // Verificar que se haya llamado al método findById del repositorio
        verify(usuarioRepository).findById(1);
    }

    @Test // test put --> actualizar usuario
    void testActualizarUsuario() {
        Rol rol = new Rol(1, "ADMIN", true, Collections.emptyList());
        Usuario usuarioExistente = new Usuario(1, "usuario123", "contrasenaSegura", true, "12345678-9", "Juan",
                "Carlos", "Pérez", "González", "juan.perez@example.com", "Av. Siempre Viva 123", "Santiago",
                "Metropolitana", rol);
        Usuario usuarioActualizado = new Usuario(1, "usuario123", "contrasenaSegura", true, "12345678-9", "Juan",
                "Carlos", "Pérez", "González", "juan.perez@example.com", "Av. Nunca Viva 456", "Santiago",
                "Metropolitana", rol);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioExistente));
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        when(usuarioRepository.save(usuarioActualizado)).thenReturn(usuarioExistente);
        Usuario resultado = usuarioService.actualizarUsuario(1, usuarioActualizado);
        assertThat(resultado.getDireccion()).isEqualTo("Av. Nunca Viva 456");
        verify(usuarioRepository).findById(1);
        verify(usuarioRepository).save(usuarioExistente);
    }

    @Test // test put --> asignar Rol
    void testAsignarRol() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        Rol rol = new Rol(1, "ADMIN", true, Collections.emptyList());
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        Usuario resultado = usuarioService.asignarRol(1, 1);
        assertThat(resultado.getRol().getNombreRol()).isEqualTo("ADMIN");
        verify(usuarioRepository).save(usuario);
    }

    @Test // desactivarUsuario
    void testDesactivarUsuario() {
        Rol rol = new Rol(1, "ADMIN", true, Collections.emptyList());
        Usuario usuario = new Usuario(1, "usuario123", "contrasenaSegura", true, "12345678-9", "Juan", "Carlos",
                "Pérez", "González", "juan.perez@example.com", "Av. Siempre Viva 123", "Santiago", "Metropolitana",
                rol);
        // Simular que el usuario ya existe en la base de datos
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        // Llamar al método desactivarUsuario
        usuarioService.desactivarUsuario(1);
        // Verificar que el usuario se haya desactivado
        assertThat(usuario.isUsuarioActivo()).isFalse();
        // Verificar que se haya guardado el usuario desactivado
        verify(usuarioRepository).save(usuario);
    }

    @Test // activarUsuario
    void testActivarUsuario() {
        Rol rol = new Rol(1, "ADMIN", true, Collections.emptyList());
        Usuario usuario = new Usuario(1, "usuario123", "contrasenaSegura", false, "12345678-9", "Juan", "Carlos",
                "Pérez", "González", "juan.perez@example.com", "Av. Siempre Viva 123", "Santiago", "Metropolitana",
                rol);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        usuarioService.activarUsuario(1);
        assertThat(usuario.isUsuarioActivo()).isTrue();
        verify(usuarioRepository).save(usuario);
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
    void testCrearUsuarioConNombreNulo() {
        Usuario usuario = new Usuario();
        usuario.setUser(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.crearUsuario(usuario));
        assertEquals("El nombre de usuario es obligatorio.", ex.getMessage());
    }

    @Test
    void testCrearUsuarioConNombreVacio() {
        Usuario usuario = new Usuario();
        usuario.setUser("");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.crearUsuario(usuario));
        assertEquals("El nombre de usuario es obligatorio.", ex.getMessage());
    }

    @Test
    void testCrearUsuarioNombreYaExistente() {
        Usuario usuario = new Usuario();
        usuario.setUser("usuarioExistente");
        when(usuarioRepository.findByUser("usuarioExistente")).thenReturn(Optional.of(new Usuario()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.crearUsuario(usuario));
        assertEquals("Ya existe un usuario con ese nombre.", ex.getMessage());
    }

    @Test
    void testCrearUsuarioConRolNoEncontrado() {
        Usuario usuario = new Usuario();
        usuario.setUser("nuevoUsuario");
        Rol rol = new Rol();
        rol.setId(99);
        usuario.setRol(rol);

        when(usuarioRepository.findByUser("nuevoUsuario")).thenReturn(Optional.empty());
        when(rolRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.crearUsuario(usuario));
        assertEquals("Rol no encontrado", ex.getMessage());
    }

    @Test
    void testCrearUsuarioConRolDesactivado() {
        Usuario usuario = new Usuario();
        usuario.setUser("nuevoUsuario");
        Rol rol = new Rol();
        rol.setId(1);
        rol.setRolActivo(false);
        usuario.setRol(rol);

        when(usuarioRepository.findByUser("nuevoUsuario")).thenReturn(Optional.empty());
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.crearUsuario(usuario));
        assertEquals("No se puede usar un rol desactivado al crear el usuario", ex.getMessage());
    }

    @Test
    void testActualizarUsuarioConRolNoEncontrado() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1);

        Usuario usuarioActualizado = new Usuario();
        Rol rol = new Rol();
        rol.setId(99);
        usuarioActualizado.setRol(rol);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioExistente));
        when(rolRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.actualizarUsuario(1, usuarioActualizado));
        assertEquals("Rol no encontrado", ex.getMessage());
    }

    @Test
    void testActualizarUsuarioConRolDesactivado() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1);

        Usuario usuarioActualizado = new Usuario();
        Rol rol = new Rol();
        rol.setId(1);
        rol.setRolActivo(false);
        usuarioActualizado.setRol(rol);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioExistente));
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.actualizarUsuario(1, usuarioActualizado));
        assertEquals("No se puede asignar un rol desactivado", ex.getMessage());
    }

    @Test
    void testActualizarUsuarioNoExistenteCreaNuevo() {
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setUser("nuevoUser");

        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Usuario resultado = usuarioService.actualizarUsuario(1, usuarioActualizado);

        assertEquals(1, resultado.getId());
        assertTrue(resultado.isUsuarioActivo());
        assertEquals("nuevoUser", resultado.getUser());
        verify(usuarioRepository).save(any());
    }

    @Test
    void testAsignarRolUsuarioNoEncontrado() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.asignarRol(1, 1));
        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void testAsignarRolNoEncontrado() {
        Usuario usuario = new Usuario();
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(rolRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.asignarRol(1, 1));
        assertEquals("Rol no encontrado", ex.getMessage());
    }

    @Test
    void testAsignarRolDesactivado() {
        Usuario usuario = new Usuario();
        Rol rol = new Rol();
        rol.setRolActivo(false);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.asignarRol(1, 1));
        assertEquals("No se puede asignar un rol desactivado", ex.getMessage());
    }

    @Test
    void testDesactivarUsuarioNoEncontrado() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.desactivarUsuario(1));
        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void testActivarUsuarioNoEncontrado() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.activarUsuario(1));
        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void testGetEstado_ActivoYDesactivo() {
        Usuario u = new Usuario();
        u.setUsuarioActivo(true);
        assertEquals("activo", u.getEstado());

        u.setUsuarioActivo(false);
        assertEquals("desactivo", u.getEstado());
    }

    @Test
    void testGetRolVisible() {
        Rol rolActivo = new Rol();
        rolActivo.setRolActivo(true);
        Usuario u = new Usuario();
        u.setRol(rolActivo);
        assertEquals(rolActivo, u.getRolVisible());

        Rol rolInactivo = new Rol();
        rolInactivo.setRolActivo(false);
        u.setRol(rolInactivo);
        assertNull(u.getRolVisible());

        u.setRol(null);
        assertNull(u.getRolVisible());
    }

    @Test
    void testListarUsuarioPorIdUsuarioNoExiste() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());
        Optional<Usuario> resultado = usuarioService.listarUsuarioPorId(99);
        assertThat(resultado).isEmpty();
        verify(usuarioRepository).findById(99);
    }

    @Test
    void testListarUsuariosListaVacia() {
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());
        List<Usuario> resultado = usuarioService.listarUsuarios();
        assertThat(resultado).isEmpty();
        verify(usuarioRepository).findAll();
    }

    @Test
    void testActualizarUsuarioActualizaTodosLosCamposNoNulos() {
        Rol rolActivo = new Rol(1, "USER", true, Collections.emptyList());
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1);
        usuarioExistente.setUser("userOld");
        usuarioExistente.setPass("passOld");
        usuarioExistente.setPrimerNombre("OldName");
        usuarioExistente.setPrimerApellido("OldLastName");
        usuarioExistente.setCorreo("old@mail.com");
        usuarioExistente.setDireccion("old address");
        usuarioExistente.setCiudad("old city");
        usuarioExistente.setRegion("old region");
        usuarioExistente.setRol(rolActivo);

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setUser("newUser");
        usuarioActualizado.setPass("newPass");
        usuarioActualizado.setPrimerNombre("newName");
        usuarioActualizado.setPrimerApellido("newLastName");
        usuarioActualizado.setCorreo("new@mail.com");
        usuarioActualizado.setDireccion("new address");
        usuarioActualizado.setCiudad("new city");
        usuarioActualizado.setRegion("new region");
        usuarioActualizado.setRol(rolActivo);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioExistente));
        when(rolRepository.findById(rolActivo.getId())).thenReturn(Optional.of(rolActivo));
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Usuario resultado = usuarioService.actualizarUsuario(1, usuarioActualizado);

        assertThat(resultado.getUser()).isEqualTo("newUser");
        assertThat(resultado.getPass()).isEqualTo("newPass");
        assertThat(resultado.getPrimerNombre()).isEqualTo("newName");
        assertThat(resultado.getPrimerApellido()).isEqualTo("newLastName");
        assertThat(resultado.getCorreo()).isEqualTo("new@mail.com");
        assertThat(resultado.getDireccion()).isEqualTo("new address");
        assertThat(resultado.getCiudad()).isEqualTo("new city");
        assertThat(resultado.getRegion()).isEqualTo("new region");
        assertThat(resultado.getRol()).isEqualTo(rolActivo);

        verify(usuarioRepository).save(any());
    }

    @Test
    void testCrearUsuarioConRolIdCero() {
        Usuario usuario = new Usuario();
        usuario.setUser("nuevoUsuario");
        Rol rol = new Rol();
        rol.setId(0); // ID inválido
        usuario.setRol(rol);

        when(usuarioRepository.findByUser("nuevoUsuario")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.crearUsuario(usuario));
        assertEquals("Rol no encontrado", ex.getMessage());
    }

    @Test
    void testCrearUsuarioConRolNulo() {
        Usuario usuario = new Usuario();
        usuario.setUser("nuevoUsuario");
        usuario.setRol(null); // Rol es null

        when(usuarioRepository.findByUser("nuevoUsuario")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.crearUsuario(usuario));
        assertEquals("Rol no encontrado", ex.getMessage());
    }

    @Test
    void testActualizarUsuarioConRolNull() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1);
        usuarioExistente.setUser("oldUser");

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setUser("newUser");
        usuarioActualizado.setRol(null); // Rol es null

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Usuario resultado = usuarioService.actualizarUsuario(1, usuarioActualizado);

        assertEquals("newUser", resultado.getUser());
        verify(usuarioRepository).save(usuarioExistente);
    }

    @Test
    void testActualizarUsuarioConRolIdCero() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1);
        usuarioExistente.setUser("oldUser");

        Rol rol = new Rol();
        rol.setId(0); // ID inválido
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setUser("newUser");
        usuarioActualizado.setRol(rol);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Usuario resultado = usuarioService.actualizarUsuario(1, usuarioActualizado);

        assertEquals("newUser", resultado.getUser());
        verify(usuarioRepository).save(usuarioExistente);
    }

}
