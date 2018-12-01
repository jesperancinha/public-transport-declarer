package com.jesperancinha.biscaje.security;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.security.Provider;
import java.security.Security;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BiscaJESecurityGeneratorImplTest {

	private static Logger logger = LoggerFactory.getLogger(BiscaJESecurityGeneratorImplTest.class);

	@Test
	public void testGenerateStrongPasswordHashAndValidatePassword1() throws Exception {
		final BiscaJESecurityGenerator biscaJESecurityGenerator = new BiscaJESecurityGeneratorImpl();

		final String hashedPassword = biscaJESecurityGenerator.generateStrongPasswordHash("Joao");

		assertTrue(biscaJESecurityGenerator.validateStrongPassword("Joao", hashedPassword));
	}

	@Test
	public void testGenerateStrongPasswordHashAndValidatePassword2() throws Exception {
		final BiscaJESecurityGenerator biscaJESecurityGenerator = new BiscaJESecurityGeneratorImpl();

		final String hashedPassword = biscaJESecurityGenerator.generateStrongPasswordHash("1234567890");

		assertTrue(biscaJESecurityGenerator.validateStrongPassword("1234567890", hashedPassword));
	}

	@Test
	public void testGenerateStrongPasswordHashAndValidatePassword3() throws Exception {
		final BiscaJESecurityGenerator biscaJESecurityGenerator = new BiscaJESecurityGeneratorImpl();

		final String hashedPassword = biscaJESecurityGenerator.generateStrongPasswordHash("1.2.3.4.5.6");

		assertTrue(biscaJESecurityGenerator.validateStrongPassword("1.2.3.4.5.6", hashedPassword));
	}

	@Test
	public void testGenerateStrongPasswordHashAndValidatePasswordNo1() throws Exception {
		final BiscaJESecurityGenerator biscaJESecurityGenerator = new BiscaJESecurityGeneratorImpl();

		final String hashedPassword = biscaJESecurityGenerator.generateStrongPasswordHash("Joao");

		assertFalse(biscaJESecurityGenerator.validateStrongPassword("oaoJ", hashedPassword));
	}

	@Test
	public void testGenerateStrongPasswordHashAndValidatePasswordNo2() throws Exception {
		final BiscaJESecurityGenerator biscaJESecurityGenerator = new BiscaJESecurityGeneratorImpl();

		final String hashedPassword = biscaJESecurityGenerator.generateStrongPasswordHash("1234567890");

		assertFalse(biscaJESecurityGenerator.validateStrongPassword("0987654321", hashedPassword));
	}

	@Test
	public void testGenerateStrongPasswordHashAndValidatePasswordNo4() throws Exception {
		final BiscaJESecurityGenerator biscaJESecurityGenerator = new BiscaJESecurityGeneratorImpl();

		final String hashedPassword = biscaJESecurityGenerator.generateStrongPasswordHash("1.2.3.4.5.6");

		assertFalse(biscaJESecurityGenerator.validateStrongPassword("6.5.4.3.2.1", hashedPassword));
	}

	/**
	 * This test only exists to see at any given time, all the providers available and their algorithms
	 */
	@Test
	public void testAllProviders() {
		final Provider[] providers = Security.getProviders();
		for (Provider provider : providers) {
			logger.info("- " + provider.getName() + ":");
			final Set<Provider.Service> services = provider.getServices();
			for (Provider.Service service : services) {
				logger.info(service.getAlgorithm());
			}
		}
	}
}