package org.acme;

import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/transferencias")
@RolesAllowed("User")
public class TransferResource {

    @Inject
    TransferService transferService;

    @POST
    public Transfer crear(Transfer transfer) {
        return transferService.crear(transfer);
    }

    @GET
    public List<Transfer> listar() {
        return transferService.listar();
    }

    @GET
    @Path("/{id}")
    public Transfer listar(Long id) {
        return transferService.listar(id);
    }

}
