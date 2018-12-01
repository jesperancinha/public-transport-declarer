package com.jesperancinha.biscaje;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BiscaJeLauncher implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(BiscaJeLauncher.class, args);
	}

	@Override public void run(String... args) {
	}
}
