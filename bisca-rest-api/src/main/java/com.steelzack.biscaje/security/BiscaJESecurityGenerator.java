package com.steelzack.biscaje.security;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
public interface BiscaJESecurityGenerator {
    abstract String generateStorngPasswordHash( //
                                                String password //
    ) throws NoSuchAlgorithmException, InvalidKeySpecException;

    String getSalt() throws NoSuchAlgorithmException;

    String toHex(byte[] array) throws NoSuchAlgorithmException;
}
