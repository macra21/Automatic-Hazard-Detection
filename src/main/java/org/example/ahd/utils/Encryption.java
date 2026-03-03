package org.example.ahd.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * Utility class that encrypts or one-way hashes different objects
 * using the guava library.
 */
public class Encryption {
    /**
     * Returns a one-way hashed string, using SHA256, of the given string.
     * @param text the text to be hashed
     * @return the hashed text
     */
    static public String SHA256OneWayHash(String text){
        return Hashing.sha256()
                .hashString(text, StandardCharsets.UTF_8)
                .toString();
    }
}
