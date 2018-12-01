package com.jesperancinha.biscaje.security;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.owasp.esapi.errors.EncryptionException;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
public interface BiscaJESecurityGenerator {

	String generateStrongPasswordHash(
			String password
	) throws NoSuchAlgorithmException, InvalidKeySpecException;

	String getSalt() throws NoSuchAlgorithmException;

	String toHex(byte[] array) throws NoSuchAlgorithmException;

	boolean validateStrongPassword(String originalPassword, String storedPassword)
			throws NoSuchAlgorithmException, InvalidKeySpecException;
}
