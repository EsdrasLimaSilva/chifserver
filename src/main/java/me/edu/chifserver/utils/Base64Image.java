package me.edu.chifserver.utils;

import java.util.Base64;

public class Base64Image {
    public static byte[] convertToByteArray(String encodedImage){
        return Base64.getDecoder().decode(encodedImage);
    }
}
