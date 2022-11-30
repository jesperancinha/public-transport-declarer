package com.jesperancinha.biscaje.server;

import com.jesperancinha.biscaje.model.User;
import com.jesperancinha.biscaje.security.BiscaJESecurityGenerator;
import com.jesperancinha.biscaje.service.UserRepository;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.time.Instant;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RequestMapping("/biscaje")
@RestController
public class BiscaRestServer {

    private final UserRepository userRepository;
    private final BiscaJESecurityGenerator biscaJESecurityGenerator;

    public BiscaRestServer(UserRepository userRepository, BiscaJESecurityGenerator biscaJESecurityGenerator) {
        this.userRepository = userRepository;
        this.biscaJESecurityGenerator = biscaJESecurityGenerator;
    }

    @GetMapping(path = "/test", produces = TEXT_PLAIN_VALUE)
    public String ping() throws InvalidKeySpecException, NoSuchAlgorithmException {
        userRepository.save(User.builder()
                .name("Joao")
                .password(biscaJESecurityGenerator.generateStrongPasswordHash("12345"))
                .lastlog(Timestamp.from(Instant.now())).build());
        return "TEST SUCCEEDED!";
    }

    @PostMapping(path = "/newuser", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createNewUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.SC_OK).body(user);
    }
}
