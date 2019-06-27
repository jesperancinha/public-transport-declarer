package com.jesperancinha.biscaje;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BiscaJeLauncher.class)
public class BiscaJeLauncherContextTest {

    @Test
    public void testContext() {

    }
}