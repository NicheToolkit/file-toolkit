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
