package com.steelzack.biscaje.server;

import com.steelzack.biscaje.model.User;
import com.steelzack.biscaje.security.BiscaJESecurityGenerator;
import com.steelzack.biscaje.service.BiscaService;
import org.apache.commons.httpclient.HttpStatus;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

/**
 * Created by joaofilipesabinoesperancinha on 16-04-16.
 */

@Path("/biscaje")
public class BiscaRestServer {

    @Inject
    private BiscaService biscaService;

    @Inject
    private BiscaJESecurityGenerator biscaJESecurityGenerator;

    @Inject
    public BiscaRestServer()
    {}


    @GET
    @Path("/test")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() throws InvalidKeySpecException, NoSuchAlgorithmException {
        biscaService.createUser("Joao", biscaJESecurityGenerator.generateStrongPasswordHash("12345"), new Date());
        return "TEST SUCCEEDED!";
    }

    @POST
    @Path("/newuser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewUser(User user) {
        return Response.status(HttpStatus.SC_OK).entity(user).build();
    }
}
