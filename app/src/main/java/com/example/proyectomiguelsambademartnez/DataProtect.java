package com.example.proyectomiguelsambademartnez;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DataProtect {
    private final static String Fkey = "AEBP145ach@~$sAC";

    public static String decryptPass(String password, String mainKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        Key secretKey = generateKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] datosDescoficados = Base64.decode(password, Base64.DEFAULT);
        byte[] datosDesencriptadosByte = cipher.doFinal(datosDescoficados);
        String datosDesencriptadosString = new String(datosDesencriptadosByte);
        return datosDesencriptadosString;
    }

    public static String encryptPass(String password, String mainKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException
    {
        Key secretKey = generateKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] datosEncriptadosBytes = cipher.doFinal(password.getBytes());
        String datosEncriptadosString = Base64.encodeToString(datosEncriptadosBytes, Base64.DEFAULT);
        return datosEncriptadosString;
    }

    private static SecretKeySpec generateKey()
            throws  UnsupportedEncodingException
    {
        byte[] key = Fkey.getBytes("UTF-8");
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        return secretKey;
    }


}
