package com.steelzack.biscaje.security;

import org.owasp.esapi.errors.EncryptionException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
public interface BiscaJESecurityGenerator {

    String generateWeakPasswordHash(String password, String accountName) throws EncryptionException, NoSuchAlgorithmException;

    String generateStrongPasswordHash( //
                                       String password //
    ) throws NoSuchAlgorithmException, InvalidKeySpecException;

    String getSalt() throws NoSuchAlgorithmException;

    String toHex(byte[] array) throws NoSuchAlgorithmException;

    boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException;
}
