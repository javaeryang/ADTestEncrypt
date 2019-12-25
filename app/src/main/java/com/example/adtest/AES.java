package com.example.adtest;

import com.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;

public class AES {


    private static byte[] iv = { 0, 9, 4, 6, 1, 7, 8, 2, 0, 9, 4, 6, 1, 7, 8, 2 };
    private static IvParameterSpec getIvParameterSpec(){
        return new IvParameterSpec(iv);
    }

    private static SecretKeySpec getSecretKeySpec(String key){
        try {
            byte[] bytes = key.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] digest = sha.digest(bytes);
            byte[] sub_digest_16 = Arrays.copyOf(digest, 16);
            return new SecretKeySpec(sub_digest_16, "AES");
        }
        catch (Throwable throwable) {
        }
        return null;
    }

    public static String encrypt(String strToEncrypt, String secret) {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(secret), getIvParameterSpec());
            return Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }
        catch (Throwable e)
        {
        }
        return null;
    }

    public static String decrypt(String strToDecrypt, String secret) {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(secret), getIvParameterSpec());
            return new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
        }
        catch (Throwable e)
        {
        }
        return null;
    }

    public static byte[] encrypt(byte[] bytesToEncrypt, String secret) {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(secret), getIvParameterSpec());
            return cipher.doFinal(bytesToEncrypt);
        }
        catch (Throwable e)
        {
        }
        return null;
    }

    public static byte[] decrypt(byte[] bytesToDecrypt, String secret) {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(secret), getIvParameterSpec());
            return cipher.doFinal(bytesToDecrypt);
        }
        catch (Throwable e)
        {
        }
        return null;
    }
}
