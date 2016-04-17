package com.steelzack.biscaje.server;

import com.steelzack.biscaje.model.User;
import com.steelzack.biscaje.service.BiscaServiceImpl;
import org.apache.commons.httpclient.HttpStatus;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

/**
 * Created by joaofilipesabinoesperancinha on 16-04-16.
 */

@Path("/biscaje")
public class BiscaRestServer {

    @Inject
    private BiscaServiceImpl biscaService;

    @GET
    @Path("test")
    @Consumes(MediaType.TEXT_PLAIN)
    public void ping()
    {
        biscaService.createUser("Joao", "12345", new Date());
    }

    @POST
    @Path("newuser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewUser(User user)
    {
        return Response.status(HttpStatus.SC_OK).entity(user).build();
    }
}
