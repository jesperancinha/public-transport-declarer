package com.jesperancinha.biscaje.server;

import com.jesperancinha.biscaje.model.User;
import com.jesperancinha.biscaje.security.BiscaJESecurityGenerator;
import com.jesperancinha.biscaje.service.UserRepository;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.time.Instant;

@Path("/biscaje")
@Controller
public class BiscaRestServer {

    private final UserRepository userRepository;

    private final BiscaJESecurityGenerator biscaJESecurityGenerator;

    @Inject
    public BiscaRestServer(UserRepository userRepository, BiscaJESecurityGenerator biscaJESecurityGenerator) {
        this.userRepository = userRepository;
        this.biscaJESecurityGenerator = biscaJESecurityGenerator;
    }

    @GET
    @Path("/test")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() throws InvalidKeySpecException, NoSuchAlgorithmException {
        userRepository.save(User.builder()
                .name("Joao")
                .password(biscaJESecurityGenerator.generateStrongPasswordHash("12345"))
                .lastlog(Timestamp.from(Instant.now())).build());
        return "TEST SUCCEEDED!";
    }

    @POST
    @Path("/newuser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewUser(User user) {
        return Response.status(HttpStatus.SC_OK).entity(user).build();
    }
}
