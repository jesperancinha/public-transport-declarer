package com.steelzack.biscaje.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.enterprise.context.ApplicationScoped;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
@ApplicationScoped
public class BiscaJESecurityGeneratorImpl implements BiscaJESecurityGenerator {

    public static final String PBKDF_2_WITH_HMAC_SHA_1 = "PBKDF2WithHmacSHA1";
    public static final String SHA_1_PRNG = "SHA1PRNG";
    int iterations;
    int saltLength;

    public BiscaJESecurityGeneratorImpl() {
        iterations = 1000;
        saltLength = 20;
    }

    @Override
    public String generateStorngPasswordHash( //
                                              String password //
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        char[] chars = password.toCharArray();
        byte[] salt = getSalt().getBytes();
        PBEKeySpec spec = new PBEKeySpec(chars, salt, this.iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF_2_WITH_HMAC_SHA_1);
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return this.iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    @Override
    public String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance(SHA_1_PRNG);
        byte[] salt = new byte[this.saltLength];
        sr.nextBytes(salt);
        return salt.toString();
    }

    @Override
    public String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }
}
