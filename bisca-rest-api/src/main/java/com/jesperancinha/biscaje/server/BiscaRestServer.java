package com.jesperancinha.biscaje.server;

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
import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.stereotype.Controller;

import com.jesperancinha.biscaje.model.User;
import com.jesperancinha.biscaje.security.BiscaJESecurityGenerator;
import com.jesperancinha.biscaje.service.UserRepository;
import org.apache.commons.httpclient.HttpStatus;

/**
 * Created by joaofilipesabinoesperancinha on 16-04-16.
 */

@Path("/biscaje")
@Controller
public class BiscaRestServer {

	@Inject
	private UserRepository userRepository;

	@Inject
	private BiscaJESecurityGenerator biscaJESecurityGenerator;

	@Inject
	public BiscaRestServer() {
	}

	@GET
	@Path("/test")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String ping() throws InvalidKeySpecException, NoSuchAlgorithmException {
		userRepository.save(com.jesperancinha.biscaje.model.User.builder()
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
