package com.zombie.utils.base;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * java正则表达式工具类
 */

public class ExpressUtils {

    private static final String getNumbers = "[0-9]+";


    public static String getNumberInString(String string) {
        Pattern pattern = Pattern.compile(getNumbers);
        Matcher matcher = pattern.matcher(string);
        matcher.find();
        return matcher.group();
    }

    public static List<String> getNumbersInString(String string) {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile(getNumbers);
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            result.add(matcher.group());
        }

        return result;
    }

    public static void main(String[] args) {
        String src = "lkjldsjg2304ljfls80985dhjg";
        // System.out.println(getNumberInString(src));
        System.out.println(getNumberInString(src));
    }
}
