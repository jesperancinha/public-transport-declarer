package com.steelzack.biscaje.server;

import com.steelzack.biscaje.model.User;
import com.steelzack.biscaje.service.BiscaService;
import org.apache.commons.httpclient.HttpStatus;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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
    private BiscaService biscaService;

    @GET
    @Path("test")
    @Consumes(MediaType.TEXT_PLAIN)
    public void ping()
    {
        try {
            EntityManager entityManager = biscaService.getEntityManager();

            final EntityTransaction transaction = entityManager.getTransaction();

            transaction.begin();

            com.steelzack.biscaje.entities.User user = new com.steelzack.biscaje.entities.User("Joao", "12345", new Date());


            entityManager.persist(user);

            transaction.commit();

            entityManager.close();
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    @POST
    @Path("newuser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewUser(User user)
    {
        return Response.status(HttpStatus.SC_OK).entity(user).build();
    }
}
