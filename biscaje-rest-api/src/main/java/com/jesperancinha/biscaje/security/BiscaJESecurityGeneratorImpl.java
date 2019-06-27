package com.jesperancinha.biscaje.security;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import static org.owasp.esapi.codecs.Hex.fromHex;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
@Service
public class BiscaJESecurityGeneratorImpl implements BiscaJESecurityGenerator {
    public static final String PBKDF_2_WITH_HMAC_SHA_512 = "PBKDF2WithHmacSHA512";
    public static final String SHA_1_PRNG = "SHA1PRNG";
    private final int iterations;
    private final int saltLength;
    private final int nKeyBytes;

    /**
     * 1000 Iterations
     * 20 Bytes salt length
     * 512 bits for key length
     */
    public BiscaJESecurityGeneratorImpl() {
        iterations = 1000;
        saltLength = 20;
        nKeyBytes = 512;
    }

    /**
     * PBKDF2 Strong password generation
     *
     * @param password Original password
     * @return Hashed/Encrypted password
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    @Override
    public String generateStrongPasswordHash( //
                                              String password //
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        char[] chars = password.toCharArray();
        byte[] salt = getSalt().getBytes();
        PBEKeySpec spec = new PBEKeySpec(chars, salt, this.iterations, nKeyBytes);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF_2_WITH_HMAC_SHA_512);
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
    public String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    @Override
    public boolean validateStrongPassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF_2_WITH_HMAC_SHA_512);
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

}
