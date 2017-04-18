package com.caccommonmodule.util;

/**
 * Created by laiis.li on 2016/12/21.
 */

public class StringUtil {

    private StringUtil() {

    }

    public static String concat(String... strs) {
        StringBuffer sb = new StringBuffer();
        for (String str : strs) {
            sb.append(str);
        }

        return sb.toString();
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String getString(String str) {
        return getDefaultString(str, "");
    }

    public static String getDefaultString(String str, String defStr) {
        if (isNullOrEmpty(str)) {
            return defStr;
        }

        return str;
    }
}