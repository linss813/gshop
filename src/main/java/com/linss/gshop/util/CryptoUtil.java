package com.linss.gshop.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class CryptoUtil {

    // 定义加密算法名称
    private static final String ALGORITHM = "AES";
    // 定义16字节的密钥，用于加密和解密操作
    private static final String KEY = "linsslinss123456";

    /**
     * 使用AES算法对字符串进行加密
     *
     * @param value 待加密的字符串
     * @return 加密后的字符串，经过Base64编码
     * @throws Exception 如果加密过程中发生错误
     */
    public static String encrypt(String value) throws Exception {
        // 创建密钥规格，使用预定义的密钥和算法
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        // 获取Cipher对象，用于执行实际的加密操作
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 初始化Cipher对象为加密模式，并传入密钥
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        // 执行加密操作，并将结果使用Base64编码以便安全传输
        byte[] encrypted = cipher.doFinal(value.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * 使用AES算法对字符串进行解密
     *
     * @param encrypted 待解密的字符串，应为Base64编码的加密数据
     * @return 解密后的字符串
     * @throws Exception 如果解密过程中发生错误
     */
    public static String decrypt(String encrypted) throws Exception {
        // 创建密钥规格，使用预定义的密钥和算法
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        // 获取Cipher对象，用于执行实际的解密操作
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 初始化Cipher对象为解密模式，并传入密钥
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        // 将Base64编码的加密数据解码为字节数组
        byte[] decoded = Base64.getDecoder().decode(encrypted);
        // 执行解密操作
        byte[] decrypted = cipher.doFinal(decoded);
        // 将解密后的字节数组转换为字符串
        return new String(decrypted);
    }
}
