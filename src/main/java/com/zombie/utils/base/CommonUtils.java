package com.zombie.utils.base;

import org.apache.http.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

/**
 * 一些常用的未分组工具类集合
 */

public class CommonUtils {

    /**
     * 将数组转成string,并以"|"对数组内容进行分割标识
     *
     * @param strArray
     * @param fixedSeparator
     *
     * @return 以"|"分割的string
     */
    public static String assembleArray(String[] strArray, boolean fixedSeparator) {

        if (strArray.length == 0) {
            return null;
        }

        String retStr = strArray[0];

        for (int i = 1; i < strArray.length; i++) {
            String str = strArray[i];

            if (fixedSeparator) {
                retStr = retStr + "|";
            } else {
                if (!ValidateHelper.isEmpty(retStr) && !ValidateHelper.isEmpty(str)) {
                    retStr = retStr + "|";
                }
            }

            retStr = retStr + str;
        }

        return retStr;
    }

    /**
     * 将NameValuePair List转换成 String以方便追加在url后<br>
     *
     * @param parameters
     *
     * @return nameValuePair的String形式
     */
    public static String format(List<NameValuePair> parameters) {
        if (ValidateHelper.isEmptyCollection(parameters))
            return null;

        final StringBuilder result = new StringBuilder();
        for (NameValuePair parameter : parameters) {
            String name = parameter.getName();
            String value = parameter.getValue();
            if (result.length() > 0) {
                result.append("&");
            }
            result.append(name);
            result.append("=");
            result.append(value);
        }
        return result.toString();
    }


    /**
     * 以GBK编码对字符串进行encode
     *
     * @param src 需要进行编码的字符串
     *
     * @return GBK编码的字符串
     */
    public static String GBKEncode(String src) {
        String strGBK = null;
        try {
            strGBK = URLEncoder.encode(src, "GBK");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return strGBK;
    }

    /**
     * 以UTF-8编码对字符串进行encode
     *
     * @param src 需要进行编码的字符串
     *
     * @return UTF-8编码的字符串
     */
    public static String UTF8Encode(String src) {
        String strUTF = "";
        try {
            strUTF = URLEncoder.encode(src, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return strUTF;
    }

    /**
     * 将枚举转换为数组
     *
     * @param t
     * @param <T>
     *
     * @return 数组对象
     */
    public static <T extends Enum<T>> Object[][] enumToArray(T[] t) {
        int length = t.length;
        Object[][] retObj = new Object[length][1];

        for (int i = 0; i < length; i++) {
            retObj[i][0] = t[i].toString();
        }

        return retObj;
    }

    /**
     * 获取所有的英文字母
     *
     * @return 英文字母数组
     */
    public static String[] getAllAlphabet() {
        String[] ret = new String[52];

        int index = 0;

        for (int i = (int) 'A'; i <= (int) 'Z'; i++) {
            ret[index] = String.valueOf((char) i);
            index++;
        }

        for (int i = (int) 'a'; i <= (int) 'z'; i++) {
            ret[index] = String.valueOf((char) i);
            index++;
        }

        return ret;
    }

    /**
     * 以UTF-8编码对string进行解码
     *
     * @param str
     *
     * @return 解码后的字符串
     */
    public static String decodeUTF8(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
