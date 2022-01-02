package org.bozdogan.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing{
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /** Converts bytes value to hexadecimal representation in string. */
    public static String bytes2hex(byte[] bytes){
        char[] hexChars = new char[bytes.length * 2]; // every byte is represented with 2 chars now.
        for(int i = 0; i < bytes.length; i++ ) {
            int v = bytes[i] & 0xFF; // 'cause chars are unsigned. doing that to make int value unsigned too.
            hexChars[i * 2] = hexArray[v >>> 4]; // retrieve the char for first 4 bits
            hexChars[i * 2 + 1] = hexArray[v & 0x0F];// retrieve the char for last 4 bits
        }

        return new String(hexChars);
    }

    /** Hash the given string with the algorithm specified
     * @param input input data
     * @param hAlgorithm hashing algorithm
     * @throws RuntimeException when specified algorithm d.n.e. */
    public static byte[] hashBytes(String input, String hAlgorithm){ // I use this to get a 128bit long key.
        try{
            return MessageDigest.getInstance(hAlgorithm).digest(input.getBytes());
        } catch(NoSuchAlgorithmException e){ throw new RuntimeException("HASHING ALGORITHM NOT FOUND"); }
    }

    /** Hashes given string to produce hex string using MD5 md5hex algorithm.
     * @param plaintext text to be hashed
     * @return 32 characters long hex string */
    public static String md5hex(String plaintext){ return bytes2hex(hashBytes(plaintext, "MD5")); }
}
