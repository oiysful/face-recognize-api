package com.ian.service;

import lombok.*;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * AES256 암호화를 위한 클래스
 * @author IAN
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter(AccessLevel.PROTECTED)
public class AES256 {
    // 암호화 알고리즘
    private static final String ALG = "AES/CBC/PKCS5Padding";
    // 암호화 Key
    private byte[] iv;

    /**
     * 업체 코드를 통해 암호화 key 생성
     * @param cpnKey 업체 코드
     * @author IAN
     */
    public AES256(String cpnKey) {
        iv = Arrays.copyOf(cpnKey.getBytes(StandardCharsets.UTF_8), 16);
    }

    /**
     * 암호화 메소드(String)
     * @param plain 암호화할 String
     * @return AES256 암호화된 String
     * @author IAN
     */
    public String encrypt(String plain) throws Exception {
        Cipher cipher = Cipher.getInstance(ALG);
        SecretKeySpec keySpec = new SecretKeySpec(iv, "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

        byte[] encrypted = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));

        return new String(Base64.getEncoder().encode(encrypted));
    }

    /**
     * 암호화 메소드(byte[])
     * @param bytes 암호화할 byte[]
     * @return AES256 암호화된 byte[]
     * @author IAN
     */
    public byte[] encrypt(byte[] bytes) throws Exception {
        Cipher cipher = Cipher.getInstance(ALG);
        SecretKeySpec keySpec = new SecretKeySpec(iv, "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

        byte[] encrypted = cipher.doFinal(bytes);

        return Base64.getEncoder().encode(encrypted);
    }

    /**
     * 복호화 메소드(String)
     * @param cipherText 복호화할 String
     * @return 복호화된 String
     * @author IAN
     */
    public String decrypt(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALG);
        SecretKeySpec keySpec = new SecretKeySpec(iv, "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    /**
     * 복호화 메소드(byte[])
     * @param cipherBytes 복호화할 byte[]
     * @return 복호화된 byte[]
     * @author IAN
     */
    public byte[] decrypt(byte[] cipherBytes) throws Exception {
        Cipher cipher = Cipher.getInstance(ALG);
        SecretKeySpec keySpec = new SecretKeySpec(iv, "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(cipherBytes);
        return cipher.doFinal(decodedBytes);
    }
}
