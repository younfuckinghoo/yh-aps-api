package com.jeesite.modules.algorithm.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.jeesite.common.base.R;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;

public class AuthUtil {

    private static final String AES_ALGORITHM = "AES";
    private static final String AES_KEY = "dsN27qC/JehGWGpYNRDW3phmwJ+2NtFcclS3xMqTcLg=";

    /**
     * 生成AES密钥
     *
     * @return Base64编码后的密钥字符串
     * @throws Exception 如果密钥生成失败
     */
    private static String generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
        keyGenerator.init(256); // 使用256位密钥长度
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * 加密字符串
     *
     * @param data      要加密的数据
     * @param keyString Base64编码的密钥字符串
     * @return 加密后的Base64字符串
     * @throws Exception 如果加密失败
     */
    private static String encrypt(String data, String keyString) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(keyString);
        SecretKey secretKey = new SecretKeySpec(keyBytes, AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 解密字符串
     *
     * @param encryptedData 加密后的Base64字符串
     * @param keyString     Base64编码的密钥字符串
     * @return 解密后的原始字符串
     * @throws Exception 如果解密失败
     */
    private static String decrypt(String encryptedData, String keyString) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(keyString);
        SecretKey secretKey = new SecretKeySpec(keyBytes, AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes, "UTF-8");
    }

    /**
     * 获取加密字符串
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    public static String authEncrypt(String username, String password) throws Exception {
        String data = username+"==="+password+"==="+System.currentTimeMillis();
        return encrypt(data, AES_KEY);
    }

    /**
     * 获取解密字符串
     * @param data
     * @return
     * @throws Exception
     */
    public static R authDecrypt(String data) throws Exception {
        String content = decrypt(data, AES_KEY);
        List<String> strList = StrUtil.split(content, "===");

        Long timestamp = Convert.toLong(strList.get(2));
        if((System.currentTimeMillis() - timestamp) > 5*60*1000){
            return R.failed("token超时");
        }
        return R.ok(content);
    }

    /**
     * 测试
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println(authEncrypt("yulang","yulang"));;
//        authDecrypt("xkviYIJLkTZyc9+Jc0sgApJDUrjdETk/OpyXcXeQEAc=");
    }

    /**
     * 加密字符串（JS）
     *
     * @param {string} data - 要加密的数据
     * @param {string} keyString - Base64编码的密钥字符串
     * @returns {string} - 加密后的Base64字符串
     */
//    function encrypt(data, keyString) {
//        try {
//            // 将Base64编码的密钥字符串解码为字节数组
//        const keyBytes = CryptoJS.Base64.decode(keyString);
//        const secretKey = CryptoJS.lib.WordArray.create(keyBytes);
//
//            // 使用AES加密
//        const encrypted = CryptoJS.AES.encrypt(data, secretKey, {
//                    mode: CryptoJS.mode.ECB, // 使用ECB模式
//                    padding: CryptoJS.pad.Pkcs7, // 使用PKCS7填充
//                    iv: CryptoJS.lib.WordArray.create(new Uint8Array(16)) // 初始化向量（ECB模式下可以忽略）
//        });
//
//            // 返回Base64编码的加密结果
//            return encrypted.toString();
//        } catch (e) {
//            console.error("加密失败:", e);
//            throw e;
//        }
//    }

}
