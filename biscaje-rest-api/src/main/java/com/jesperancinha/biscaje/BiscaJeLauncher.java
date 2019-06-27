package com.jesperancinha.biscaje;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EntityScan("com.jesperancinha.biscaje.model")
@EnableJpaRepositories("com.jesperancinha.biscaje.service")
public class BiscaJeLauncher implements CommandLineRunner {

    public static void main(String[] args) {
        try {
            SpringApplication.run(BiscaJeLauncher.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(String... args) {

    }
}
