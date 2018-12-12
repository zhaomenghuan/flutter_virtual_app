package com.lody.virtual.helper.utils;

import android.util.Base64;

/**
 * Author: zhaomenghuan
 * Email: zhaomenghuan@agree.com.cn
 * Date：2018/11/1.
 */

public class EncodeUtils {
    public static String decode(String base64) {
        return new String(Base64.decode(base64, 0));
    }
}
