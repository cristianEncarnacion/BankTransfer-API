package org.acme;

import java.util.Arrays;
import java.util.HashSet;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import io.smallrye.jwt.build.Jwt;

@Path("/auth")
public class UserResource {

    @Inject
    UserService userService;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(UserCredentials credentials) {
        if (userService.verificarCredenciales(credentials.username, credentials.password)) {
            String token = Jwt.issuer("banktransfer")
                    .upn(credentials.username)
                    .groups(new HashSet<>(Arrays.asList("User")))
                    .sign();
            return Response.ok(token).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("Credenciales incorrectas").build();
    }

}
