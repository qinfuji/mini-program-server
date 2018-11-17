package com.bczl.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5工具
 * <p>
 * Created by licheng1 on 2017/4/21.
 */
public class MD5Utils {

    public static String getMd5(String plainText) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(plainText.getBytes());
        byte b[] = md.digest();

        int i;

        StringBuilder buf = new StringBuilder();
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0)
                i += 256;
            if (i < 16)
                buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        //32位加密
        return buf.toString();
    }
}
