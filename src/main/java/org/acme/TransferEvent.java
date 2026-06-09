package org.acme;

import java.math.BigDecimal;

public record TransferEvent(Long id, String cuentaOrigen, String cuentaDestino, BigDecimal monto) {

}
