package com.example.proyectomiguelsambademartnez;

import android.util.Base64;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test1() throws UnsupportedEncodingException {
        String data = "AEBP145ach@~$sAC";
        byte[] key = data.getBytes("UTF-8");

    }

    @Test
    public void test2(){
    }
}