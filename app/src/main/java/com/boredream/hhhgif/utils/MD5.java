package com.boredream.hhhgif.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String encode(String src) {
        String encodeStr = null;
        if (src != null) {
            try {
                byte[] bytes = MessageDigest.getInstance("MD5").digest(src.getBytes("UTF-8"));
                encodeStr = new String(bytes, "UTF-8");
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return encodeStr;
    }
}
