package org.acme;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
class TransferResourceTest {

    @InjectMock
    TransferService transferService;

    private Transfer transferencia(Long id, String origen, String destino, String monto) {
        Transfer t = new Transfer(origen, destino, new BigDecimal(monto), "PENDIENTE",
                LocalDateTime.of(2026, 1, 15, 10, 30));
        t.id = id;
        return t;
    }

    @Test
    @TestSecurity(user = "cliente1", roles = "cliente_banca")
    void crearTransferenciaDevuelveLaTransferenciaCreada() {
        when(transferService.crear(any(Transfer.class)))
                .thenReturn(transferencia(1L, "1001234567", "2009876543", "1500.00"));

        given()
                .contentType(ContentType.JSON)
                .body("{\"cuentaOrigen\":\"1001234567\",\"cuentaDestino\":\"2009876543\",\"monto\":1500.00}")
                .when().post("/transferencias")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("cuentaOrigen", equalTo("1001234567"))
                .body("estado", equalTo("PENDIENTE"));

        verify(transferService).crear(any(Transfer.class));
    }

    @Test
    @TestSecurity(user = "cliente1", roles = "cliente_banca")
    void listarDevuelveTodasLasTransferencias() {
        when(transferService.listar()).thenReturn(List.of(
                transferencia(1L, "1001234567", "2009876543", "1500.00"),
                transferencia(2L, "1001234567", "3001112223", "250.50")));

        given()
                .when().get("/transferencias")
                .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("[1].cuentaDestino", equalTo("3001112223"));
    }

    @Test
    @TestSecurity(user = "cliente1", roles = "cliente_banca")
    void obtenerPorIdDevuelveLaTransferencia() {
        when(transferService.listar(5L)).thenReturn(transferencia(5L, "1001234567", "2009876543", "99.99"));

        given()
                .when().get("/transferencias/5")
                .then()
                .statusCode(200)
                .body("id", equalTo(5))
                .body("monto", equalTo(99.99f));
    }

    @Test
    void sinAutenticacionDevuelve401() {
        given()
                .when().get("/transferencias")
                .then()
                .statusCode(401);

        verify(transferService, never()).listar();
    }

    @Test
    @TestSecurity(user = "intruso", roles = "otro_rol")
    void conRolIncorrectoDevuelve403() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"cuentaOrigen\":\"1\",\"cuentaDestino\":\"2\",\"monto\":10}")
                .when().post("/transferencias")
                .then()
                .statusCode(403);

        verify(transferService, never()).crear(any(Transfer.class));
    }
}
