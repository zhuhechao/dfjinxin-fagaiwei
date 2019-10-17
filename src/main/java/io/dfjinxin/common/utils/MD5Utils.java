package io.dfjinxin.common.utils;

import org.apache.commons.codec.digest.DigestUtils;
/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2019/10/17 10:17
 * @Version: 1.0
 */
public class MD5Utils {

    public static String getMD5(String str) {
//        String base = str;
        String md5 = DigestUtils.md5Hex(str);
        return md5;
    }
}
