package org.acme;

import java.util.List;

import org.eclipse.microprofile.reactive.messaging.Channel;

import org.eclipse.microprofile.reactive.messaging.Emitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TransferService {

    @Inject
    @Channel("transferencias-out")
    Emitter<TransferEvent> emitter;

    @Transactional
    public Transfer crear(Transfer transfer) {
        transfer.persist();

        emitter.send(new TransferEvent(
                transfer.id,
                transfer.cuentaOrigen,
                transfer.cuentaDestino,
                transfer.monto)).toCompletableFuture().join();
        return transfer;
    }

    public List<Transfer> listar() {
        return Transfer.listAll();
    }

    public Transfer listar(Long id) {
        return Transfer.findById(id);
    }
}
