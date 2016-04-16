package com.steelzack.biscaje.server;

import com.steelzack.biscaje.model.User;
import org.apache.commons.httpclient.HttpStatus;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by joaofilipesabinoesperancinha on 16-04-16.
 */

@Path("/biscaje")
public class BiscaRestServer {

    @POST
    @Path("newuser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewUser(User user)
    {
        return Response.status(HttpStatus.SC_OK).entity(user).build();
    }
}
