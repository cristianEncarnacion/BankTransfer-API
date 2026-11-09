package org.acme;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class UserTest {

    @BeforeEach
    void setUp() {
        PanacheMock.mock(User.class);
        // Solo se mockea el acceso a datos; la lógica de credenciales es la real
        when(User.verificarCredenciales(anyString(), anyString())).thenCallRealMethod();
        when(User.existeUsuario(anyString())).thenCallRealMethod();
        when(User.nuevoUsuario(anyString(), anyString())).thenCallRealMethod();
    }

    @Test
    void nuevoUsuarioGuardaLaContrasenaHasheada() {
        User user = User.nuevoUsuario("cristian", "Secreta123!");

        assertEquals("cristian", user.nombre);
        assertNotEquals("Secreta123!", user.password);
        assertTrue(user.password.startsWith("$2"), "Debe ser un hash BCrypt");
    }

    @Test
    void toStringNoExponeLaContrasena() {
        User user = User.nuevoUsuario("cristian", "Secreta123!");

        assertFalse(user.toString().contains(user.password));
        assertFalse(user.toString().contains("Secreta123!"));
    }

    @Test
    void verificarCredencialesAceptaLaContrasenaCorrecta() {
        when(User.findByNombre("cristian")).thenReturn(User.nuevoUsuario("cristian", "Secreta123!"));

        assertTrue(User.verificarCredenciales("cristian", "Secreta123!"));
    }

    @Test
    void verificarCredencialesRechazaLaContrasenaIncorrecta() {
        when(User.findByNombre("cristian")).thenReturn(User.nuevoUsuario("cristian", "Secreta123!"));

        assertFalse(User.verificarCredenciales("cristian", "otraClave"));
    }

    @Test
    void verificarCredencialesRechazaUsuarioInexistente() {
        when(User.findByNombre("fantasma")).thenReturn(null);

        assertFalse(User.verificarCredenciales("fantasma", "loQueSea"));
        assertFalse(User.existeUsuario("fantasma"));
    }
}
