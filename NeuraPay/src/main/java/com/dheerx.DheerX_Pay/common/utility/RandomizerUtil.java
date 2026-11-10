package com.dheerx.DheerX_Pay.common.utility;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomizerUtil {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generate() {
        byte[] bef = new byte[8];
        SECURE_RANDOM.nextBytes(bef);
        return "MAC"+Base64.getUrlEncoder().withoutPadding().encodeToString(bef);
    }
    public static String randomBase64(int length){
         byte[] bef = new byte[length];
        SECURE_RANDOM.nextBytes(bef);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bef);
    }
}