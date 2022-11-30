package com.jesperancinha.biscaje;

import com.jesperancinha.biscaje.containers.AbstractTestContainersIT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ContextConfiguration(initializers = AbstractTestContainersIT.DockerPostgresDataInitializer.class)
public class BiscaJeLauncherContextTest {

    @Test
    public void testContext() {

    }
}