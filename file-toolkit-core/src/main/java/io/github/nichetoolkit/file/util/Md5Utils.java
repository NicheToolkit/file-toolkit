package io.github.nichetoolkit.file.util;

import io.github.nichetoolkit.rest.util.GeneralUtils;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

/**
 * <p>Md5Utils</p>
 * @author Cyan (snow22314@outlook.com)
 * @version v1.0.0
 */
public class Md5Utils {

    /**
     * 获取一个文件的md5值(可处理大文件)
     * @return md5 value
     */
    public static String md5(File file) {
        FileInputStream fileInputStream = null;
        try {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(MD5.digest()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String md5(byte[] bytes) {
        if (GeneralUtils.isEmpty(bytes)) {
            return null;
        }
        return DigestUtils.md2Hex(bytes);
    }

    /**
     * 求一个字符串的md5值
     * @param target 字符串
     * @return md5 value
     */
    public static String md5(String target) {
        if (GeneralUtils.isEmpty(target)) {
            return null;
        }
        return DigestUtils.md5Hex(target);
    }
}
