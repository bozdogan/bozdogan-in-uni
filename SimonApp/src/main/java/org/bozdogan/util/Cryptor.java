package org.bozdogan.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;
import java.nio.charset.Charset;

/** <p>Brief: Used for encrypting some text or binary and encode it so that it can be
 * transferred as text uncorrupted.</p> */
public class Cryptor{
    public final static String VERSION = "1.3";
    public final static String DEFAULT_ALGORITHM = "AES/ECB/PKCS5Padding";
    private String hashedPassword;

    private Cipher cipher;
    private SecretKey key;

    /** Constructor with password parameter. Default algorithm will be used.
     * @param password password to use with encryption */
    public Cryptor(String password){ this(password, DEFAULT_ALGORITHM); }

    /** Constructor with password and algorithm parameters.
     * @param password password to use with encryption
     * @param algorithm algorithm to use for encryption
     * @throws InitiationException when algorithm given is not recognised. */
    public Cryptor(String password, String algorithm){
        int indexOfSlash = algorithm.indexOf('/');
        String algorithmName = indexOfSlash==-1 ? algorithm : algorithm.substring(0, indexOfSlash);


        try{ cipher = Cipher.getInstance(algorithm); /* Initialize cipher object */ }
        catch(GeneralSecurityException err){ throw new InitiationException("NO SUCH ALGORITHM: "+algorithm); }


        // Create a key object for encryption. Key object creation requires 128-bit unique
        // password. To provide this, I hash the password with MD5.
        key = new SecretKeySpec(Hashing.hashBytes(password, "MD5"), algorithmName);
        hashedPassword = Hashing.md5hex(password);
    }

    /** Get the hashed password. */
    public String getHashedPassword(){ return hashedPassword; }

    /** Get the algorithm definition. */
    public String getAlgorithm(){ return cipher.getAlgorithm(); }

    /** Encrypt the text using the set algorithm.
     * @param plaintext text to be encrypted
     * @return ciphertext */
    public String encrypt(String plaintext){
        // init cipher.
        // character encode plaintext to bytes. encrypt it using cipher.
        // Base64 encode it to string.
        try{
            cipher.init(Cipher.ENCRYPT_MODE, key);

            return Base64.getEncoder().encodeToString(
                    cipher.doFinal(plaintext.getBytes(Charset.forName("UTF-8")))
            );
        } catch(GeneralSecurityException e){ throw new RuntimeException("ENCRYPTION ERROR"); }
    }

    /** Decrypt the text using the set algorithm.
     * @param ciphertext text to be decrypted
     * @return plaintext or {@code null} if Base64 decryption fails */
    public String decrypt(String ciphertext){
        // init cipher.
        // Base64 decode ciphertext to bytes. decrypt it using cipher.
        // character decode it to string.
        try{
            cipher.init(Cipher.DECRYPT_MODE, key);

            return new String(cipher.doFinal(
                    Base64.getDecoder().decode(ciphertext)
            ), Charset.forName("UTF-8"));
        } catch(GeneralSecurityException e){ e.printStackTrace(); throw new RuntimeException("DECRYPTION ERROR");
        } catch(IllegalArgumentException e){ return null; } // base64 decryption error. possibly wrong pw.
    }


    @Override
    public boolean equals(Object obj){
        return obj instanceof Cryptor &&
                ((Cryptor) obj).hashedPassword.equals(hashedPassword) &&
                ((Cryptor) obj).getAlgorithm().equals(this.getAlgorithm());
    }

    public static class InitiationException extends RuntimeException{
        public InitiationException(String msg){ super(msg); }
    }
}

/*
 * ## CObject 1.1 Changelog
 * - Character encoding on encryption and decryption explicitly specified as UTF-8
 *   Platforms default charset was used previously. This caused issues as Windows
 *   uses a codepage specific to locale. That reduces the portability so, it must
 *   be fixed.
 *
 * ## CObject 1.2 Changelog
 * - Support for specifying password in constructor.
 * - More detailed exception handling with the new ´util.InitiationException´
 *
 * ## Cryptor 1.3 Changelog
 * - Name changed form 'CObject' to 'Cryptor'.
 * - Helper methods are moved to ´Hashing´ class.
 * - Default constructor is removed, thus master password can no longer change after
 *   instantiation.
 * - CipherInitiationException became obsolete. Use Cryptor.InitiationException instead.
 * */