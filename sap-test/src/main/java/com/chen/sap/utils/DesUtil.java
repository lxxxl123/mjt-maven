package com.chen.sap.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.security.Key;
import java.security.SecureRandom;

/**
 * @author yangzj2
 */
public class DesUtil {
    private static String strDefaultKey = "gaojinsoft";
    private static final int ARR_LEN_2 = 2;

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[] hexStr2ByteArr(String strIn) 互为可逆的转换过程
     * @param arrB 需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception
     *             本方法不处理任何异常，所有异常全部抛出
     */
    public static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB) 互为可逆的转换过程
     * @param strIn 需要转换的字符串
     * @return 转换后的byte数组
     * @throws Exception
     *             本方法不处理任何异常，所有异常全部抛出
     * @author
     */
    public static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / ARR_LEN_2];
        for (int i = 0; i < iLen; i = i + ARR_LEN_2) {
            String strTmp = new String(arrB, i, ARR_LEN_2);
            arrOut[i / ARR_LEN_2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * 加密字节数组
     * @param arrB 需加密的字节数组
     * @return 加密后的字节数组
     * @throws Exception
     */
    public static byte[] encrypt(byte[] arrB) throws Exception {
        return encrypt(arrB,strDefaultKey);
    }

    /**
     * 加密字符串
     * @param strIn 需加密的字符串
     * @return 加密后的字符串
     * @throws Exception
     */
    public static String encrypt(String strIn) throws Exception {
        return byteArr2HexStr(encrypt(strIn.getBytes()));
    }

    /**
     * 加密字节数组
     * @param arrB 需加密的字节数组
     * @param strKey 自定义密钥
     * @return 加密后的字节数组
     * @throws Exception
     */
    public static byte[] encrypt(byte[] arrB,String strKey) throws Exception {
        Key key = getKey(strKey);
        Cipher encryptCipher = Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        return encryptCipher.doFinal(arrB);
    }

    /**
     * 加密字符串
     * @param strIn 需加密的字符串
     * @param strKey 自定义密钥
     * @return 加密后的字符串
     * @throws Exception
     */
    public static String encrypt(String strIn,String strKey) throws Exception {
        return byteArr2HexStr(encrypt(strIn.getBytes(),strKey));
    }

    /**
     * 解密字节数组
     * @param arrB 需解密的字节数组
     * @param strKey 自定义密钥
     * @return 解密后的字节数组
     * @throws Exception
     */
    public static byte[] decrypt(byte[] arrB,String strKey) throws Exception {
        Key key = getKey(strKey);
        Cipher decryptCipher = Cipher.getInstance("DES");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
        return decryptCipher.doFinal(arrB);
    }

    /**
     * 解密字符串
     * @param strIn 需解密的字符串
     * @param strKey 自定义密钥
     * @return 解密后的字符串
     * @throws Exception
     */
    public static String decrypt(String strIn,String strKey) throws Exception {
        return new String(decrypt(hexStr2ByteArr(strIn),strKey));
    }

    /**
     * 解密字节数组
     * @param arrB 需解密的字节数组
     * @return 解密后的字节数组
     * @throws Exception
     */
    public static byte[] decrypt(byte[] arrB) throws Exception {
        return decrypt(arrB,strDefaultKey);
    }

    /**
     * 解密字符串
     * @param strIn 需解密的字符串
     * @return 解密后的字符串
     * @throws Exception
     */
    public static String decrypt(String strIn) throws Exception {
        return new String(decrypt(hexStr2ByteArr(strIn)));
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     * @param strKey 构成该字符串的字节数组
     * @return 生成的密钥
     * @throws Exception
     */
    private static Key getKey(String strKey) throws Exception {
        byte[] arrByteTmp = strKey.getBytes();
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];
        // 将原始字节数组转换为8位
        for (int i = 0; i < arrByteTmp.length && i < arrB.length; i++) {
            arrB[i] = arrByteTmp[i];
        }
        // 生成密钥
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
        return key;
    }

    /**
     * DES加密(ECB方式，与.net互通)
     */
    public static byte[] desEncryptDotNet(byte[] plainText, byte[] desKey) throws Exception {
        SecureRandom sr = new SecureRandom();
        byte rawKeyData[] = desKey;
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);
        byte data[] = plainText;
        byte encryptedData[] = cipher.doFinal(data);
        return encryptedData;
    }

    /**
     * DES解密(ECB方式，与.net互通)
     */
    public static byte[] desDecryptDotNet(byte[] encryptText, byte[] desKey) {
        try {
            SecureRandom sr = new SecureRandom();
            byte rawKeyData[] = desKey;
            DESKeySpec dks = new DESKeySpec(rawKeyData);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key, sr);
            byte encryptedData[] = encryptText;
            byte decryptedData[] = cipher.doFinal(encryptedData);
            return decryptedData;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 解密C#的数据(ECB方式，与.net互通)
     */
    public static String decryptDotNet(String desStr, String desKey) {
        try {
            byte[] result = base64Decode(desStr);
            if(result == null) {
                return null;
            }
            return new String(desDecryptDotNet(result, desKey.getBytes()));
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 加密C#的数据(ECB方式，与.net互通)
     */
    public static String encryptDotNet(String str, String desKey) {
        try {
            return base64Encode(desEncryptDotNet(str.getBytes(), desKey.getBytes()));
        } catch (Exception ex) {
            return null;
        }
    }

    public static String base64Encode(byte[] s) {
        if (s == null) {
            return null;
        }
        BASE64Encoder b = new BASE64Encoder();
        return b.encode(s);
    }

    public static byte[] base64Decode(String s) throws IOException {
        if (s == null) {
            return null;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b = decoder.decodeBuffer(s);
        return b;
    }

    public static void encryptLog(String s) throws Exception {
        System.out.println("加密前的字符 = " + s);
        System.out.println("加密后的字符 = " + encrypt(s));
    }


    public static void decryptLog(String s) throws Exception {
        System.out.println("加密前的字符 = " + s);
        System.out.println("加密后的字符 = " + decrypt(s));
    }

    public static  void entry(){
        try {
            encryptLog("zcallrfc");
            encryptLog("123456");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void decrypt() {
        try {
            decryptLog("460a82970d683555");
            decryptLog("28c33be7af472179ea1acb0b430d5dd5f5f231e7ea3316b6");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        decrypt();
    }
}
