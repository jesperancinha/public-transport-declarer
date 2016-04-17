package com.steelzack.biscaje.security;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
public class BiscaJESecurityGeneratorImplTest {
    @Test
    public void generateStrongPasswordHashAndValidatePassword1() throws Exception {
        final BiscaJESecurityGenerator biscaJESecurityGenerator = new BiscaJESecurityGeneratorImpl();

        final String hashedPassword = biscaJESecurityGenerator.generateStrongPasswordHash("Joao");

        assertTrue(biscaJESecurityGenerator.validatePassword("Joao", hashedPassword));
    }

    @Test
    public void generateStrongPasswordHashAndValidatePassword2() throws Exception {
        final BiscaJESecurityGenerator biscaJESecurityGenerator = new BiscaJESecurityGeneratorImpl();

        final String hashedPassword = biscaJESecurityGenerator.generateStrongPasswordHash("1234567890");

        assertTrue(biscaJESecurityGenerator.validatePassword("1234567890", hashedPassword));
    }

    @Test
    public void generateStrongPasswordHashAndValidatePassword3() throws Exception {
        final BiscaJESecurityGenerator biscaJESecurityGenerator = new BiscaJESecurityGeneratorImpl();

        final String hashedPassword = biscaJESecurityGenerator.generateStrongPasswordHash("1.2.3.4.5.6");

        assertTrue(biscaJESecurityGenerator.validatePassword("1.2.3.4.5.6", hashedPassword));
    }

    @Test
    public void generateStrongPasswordHashAndValidatePasswordNo1() throws Exception {
        final BiscaJESecurityGenerator biscaJESecurityGenerator = new BiscaJESecurityGeneratorImpl();

        final String hashedPassword = biscaJESecurityGenerator.generateStrongPasswordHash("Joao");

        assertFalse(biscaJESecurityGenerator.validatePassword("oaoJ", hashedPassword));
    }

    @Test
    public void generateStrongPasswordHashAndValidatePasswordNo2() throws Exception {
        final BiscaJESecurityGenerator biscaJESecurityGenerator = new BiscaJESecurityGeneratorImpl();

        final String hashedPassword = biscaJESecurityGenerator.generateStrongPasswordHash("1234567890");

        assertFalse(biscaJESecurityGenerator.validatePassword("0987654321", hashedPassword));
    }

    @Test
    public void generateStrongPasswordHashAndValidatePasswordNo4() throws Exception {
        final BiscaJESecurityGenerator biscaJESecurityGenerator = new BiscaJESecurityGeneratorImpl();

        final String hashedPassword = biscaJESecurityGenerator.generateStrongPasswordHash("1.2.3.4.5.6");

        assertFalse(biscaJESecurityGenerator.validatePassword("6.5.4.3.2.1", hashedPassword));
    }
}