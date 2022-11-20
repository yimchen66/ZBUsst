package com.example.zbusst.Util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Author 陈一鸣
 * @ClassName EnCodeToolsUtil
 * @Description 加密、解密  md5、Base64、hash
 * @date 2022/10/27 9:24
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class EnCodeToolsUtil {
    final static Base64.Encoder encoder = Base64.getEncoder();
    final static Base64.Decoder decoder = Base64.getDecoder();
    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";


    public static String getMd5(String oristring){
        /**
         * @Description: TODO
         * @Params: oristring 原字符串
         * @Return  md5str md5加密后的字符
         */

        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest  = md5.digest(oristring.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //16是表示转换为16进制数
        String md5Str = new BigInteger(1, digest).toString(16);

        return md5Str;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getBase64Encode(byte[] bys){
        String base64str = encoder.encodeToString(bys);
        return base64str;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getBase64Decode(String oristr){
        String str = new String(decoder.decode(oristr), StandardCharsets.UTF_8);
        return str;
    }

    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey){
        byte[] data= new byte[0];
        try {
            data = encryptKey.getBytes(ENCODING);
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
            SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance(MAC_NAME);
            //用给定密钥初始化 Mac 对象
            mac.init(secretKey);
            byte[] text = encryptText.getBytes(ENCODING);
            //完成 Mac 操作
            return mac.doFinal(text);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }



    }



}
