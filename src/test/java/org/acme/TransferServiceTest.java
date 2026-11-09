package org.acme;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;

@QuarkusTest
class TransferServiceTest {

    @Inject
    TransferService transferService;

    @Inject
    @Any
    InMemoryConnector connector;

    private InMemorySink<TransferEvent> sink;

    @BeforeEach
    void setUp() {
        sink = connector.sink("transferencias-out");
        sink.clear();
    }

    @Test
    void crearPersisteYPublicaElEventoEnKafka() {
        Transfer transfer = spy(new Transfer("1001234567", "2009876543", new BigDecimal("1500.00"),
                "PENDIENTE", LocalDateTime.now()));
        transfer.id = 42L;
        doNothing().when(transfer).persist();

        Transfer resultado = transferService.crear(transfer);

        assertSame(transfer, resultado);
        verify(transfer).persist();

        assertEquals(1, sink.received().size());
        TransferEvent evento = sink.received().get(0).getPayload();
        assertEquals(42L, evento.id());
        assertEquals("1001234567", evento.cuentaOrigen());
        assertEquals("2009876543", evento.cuentaDestino());
        assertEquals(new BigDecimal("1500.00"), evento.monto());
    }

    @Test
    void listarDelegaEnPanache() {
        PanacheMock.mock(Transfer.class);
        Transfer t = new Transfer("1", "2", BigDecimal.TEN, "PENDIENTE", LocalDateTime.now());
        when(Transfer.listAll()).thenReturn(List.of(t));

        List<Transfer> resultado = transferService.listar();

        assertEquals(1, resultado.size());
        assertSame(t, resultado.get(0));
    }

    @Test
    void listarPorIdDelegaEnPanache() {
        PanacheMock.mock(Transfer.class);
        Transfer t = new Transfer("1", "2", BigDecimal.ONE, "PENDIENTE", LocalDateTime.now());
        when(Transfer.findById(7L)).thenReturn(t);

        assertSame(t, transferService.listar(7L));
    }
}
