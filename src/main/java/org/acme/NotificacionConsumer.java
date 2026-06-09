package org.acme;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NotificacionConsumer {

    @Incoming("transferencias-in")
    public void onTransferencia(TransferEvent evento) {
        System.out.println("🔔 Notificación: Transferencia #" + evento.id() + " de RD$" + evento.monto()
                + " procesada exitosamente.");
    }

}