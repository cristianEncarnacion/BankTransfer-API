package org.acme;

import java.util.List;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/transferencias")
@RolesAllowed("cliente_banca")
public class TransferResource {

    @Inject
    TransferService transferService;

    @POST
    @CacheInvalidateAll(cacheName = "transferencias-lista")
    public Transfer crear(Transfer transfer) {
        return transferService.crear(transfer);
    }

    @GET
    @CacheResult(cacheName = "transferencias-lista")
    public List<Transfer> listar() {
        return transferService.listar();
    }

    @GET
    @Path("/{id}")
    @CacheResult(cacheName = "transferencias-detalle")
    public Transfer listar(@PathParam("id") Long id) {
        return transferService.listar(id);
    }

}
