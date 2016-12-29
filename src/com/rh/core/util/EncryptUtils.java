/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.core.util;

import java.security.MessageDigest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.util.encrypt.DesPKCS7;

/**
 * 关于加解密的工具类 需要额外包：jce.zip
 * @author Jerry Li
 */
public class EncryptUtils {
    /** MD5加密，不可还原 */
    public static final String MD5 = "MD5";
    /** SHA加密，不可还原 */
    public static final String SHA = "SHA";
    /** DES加密，可以还原 */
    public static final String DES = "DES";

    /**
     * DES加密密钥，解密时也要用到 该变量不可以改变，否则将无法对密码进行解密
     */
    private static final String PASSWORD_CRYPT_KEY = "theta networks";
    
    /**
     * DES加密偏移量，最少8字节
     */
    private static final String PASSWORD_INIT_VECTOR = "opoejkls";
    
    /** log */
    private static Log log = LogFactory.getLog(EncryptUtils.class);

    /**
     * 按照加密类型加密字串
     * @param orgStr 要加密的字串
     * @param encType 加密类型
     * @return 加密后的字串，如果没有匹配的加密方式，则返回原字串
     */
    public static String encrypt(String orgStr, String encType) {
        try {
            if ((encType.compareToIgnoreCase("SHA") == 0)) {
                MessageDigest md = MessageDigest.getInstance(encType);
                byte[] digest = md.digest(orgStr.getBytes());
                return new String(Lang.encodeBase64(digest));
            } else if (encType.compareToIgnoreCase("MD5") == 0) {
                MessageDigest alga = MessageDigest.getInstance("MD5");
                alga.update(orgStr.getBytes());
                byte[] digesta = alga.digest();
                return Lang.byteTohex(digesta);
            } else if (encType.compareToIgnoreCase("DES") == 0) {
                return desEncrypt(orgStr);
            }
            return orgStr;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return orgStr;
        }
    }

    /**
     * 按照解密密类型解密字串
     * @param encryptedStr 要解密的字串
     * @param encType 解密类型
     * @return 解密后的字串，如果不是DES方式，则返回原字串
     */
    public static String decrypt(String encryptedStr, String encType) {
        if ("DES".equals(encType)) {
            try {
                return desDecrypt(encryptedStr);
            } catch (Exception ex) {
                return encryptedStr;
            }
        }
        return encryptedStr;
    }

    /**
     * 数据解密
     * @param data 待解密的数据
     * @return  解密后的数据
     */
    public static String desDecrypt(String data) {
    		try {
    		    return new DesPKCS7(PASSWORD_CRYPT_KEY, PASSWORD_INIT_VECTOR).decrypt(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 数据加密
     * @param data 待加密的数据
     * @return 加密后的数据
     */
    public static String desEncrypt(String data) {
    		try {
    		    return new DesPKCS7(PASSWORD_CRYPT_KEY, PASSWORD_INIT_VECTOR).encrypt(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 解密用户密码
     * @param args 参数
     */
    public static void main(String[] args) {
        String mima = "cf7e2948b56067fc";
        String mingma = EncryptUtils.desDecrypt(mima);
        String ss =EncryptUtils.encrypt("1qaz@WSX",EncryptUtils.DES);
        System.out.println(ss);
    }
    
}
