package com.zombie.utils.base;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * 随机数工具类
 */
public class RandomUtils {

    private static final String ALL_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LETTER_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBER_CHAR = "0123456789";

    /**
     * 获取定长的随机数，包含大小写、数字
     *
     * @param length 随机数长度
     *
     * @return
     */
    public static String generateString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(ALL_CHAR.charAt(random.nextInt(ALL_CHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 获取定长的随机数，包含大小写字母
     *
     * @param length 随机数长度
     *
     * @return 随机数
     */
    public static String generateMixString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(LETTER_CHAR.charAt(random.nextInt(LETTER_CHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 获取定长的随机数，只包含小写字母
     *
     * @param length 随机数长度
     *
     * @return 随机数
     */
    public static String generateLowerString(int length) {
        return generateMixString(length).toLowerCase();
    }

    /**
     * 获取定长的随机数，只包含大写字母
     *
     * @param length 随机数长度
     *
     * @return 随机数
     */
    public static String generateUpperString(int length) {
        return generateMixString(length).toUpperCase();
    }

    /**
     * 获取定长的随机数，只包含数字
     *
     * @param length 随机数长度
     *
     * @return 随机数
     */
    public static String generateNumberString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(NUMBER_CHAR.charAt(random.nextInt(NUMBER_CHAR.length())));
        }
        String result = sb.toString();
        if (result.startsWith("0")) {
            result = result.replaceFirst("0", "1");
        }
        return result;
    }

    /**
     * 生成指定范围的随机数[0,range)
     *
     * @param range 随机数范围
     *
     * @return 随机数
     */
    public static int generateRandomInt(int range) {
        Random random = new Random();
        return random.nextInt(range);
    }


    private static String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153,156,177,180,181,182,183,185,188".split(",");

    /**
     * 生成随机的电话号码
     *
     * @return 字符串形式的手机号
     */
    public static String getTelNum() {
        int index = getPhoneNum(0, telFirst.length - 1);
        String first = telFirst[index];
        String second = String.valueOf(getPhoneNum(1, 888) + 10000).substring(1);
        String thrid = String.valueOf(getPhoneNum(1, 9100) + 10000).substring(1);
        return first + second + thrid;
    }

    /**
     * 生成191开头的手机号
     *
     * @return
     */
    public static String getSpecPhoneNumber() {
        String first = "191";
        String second = String.valueOf(getPhoneNum(1, 888) + 10000).substring(1);
        String thrid = String.valueOf(getPhoneNum(1, 9100) + 10000).substring(1);
        return first + second + thrid;
    }

    private static int getPhoneNum(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    public static String getRandomJianHan(int len) {
        String ret = "";
        for (int i = 0; i < len; i++) {
            String str = null;
            int hightPos, lowPos; // 定义高低位
            Random random = new Random();
            hightPos = (176 + Math.abs(random.nextInt(39))); //获取高位值
            lowPos = (161 + Math.abs(random.nextInt(93))); //获取低位值
            byte[] b = new byte[2];
            b[0] = (new Integer(hightPos).byteValue());
            b[1] = (new Integer(lowPos).byteValue());
            try {
                str = new String(b, "GBK"); //转成中文
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            ret += str;
        }
        return ret;
    }

    public static void main(String[] args) {
        System.out.println(getSpecPhoneNumber());
    }
}
