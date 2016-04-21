package com.zombie.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

/**
 * FastJson工具类
 */
public class FastJsonUtil {

    /**
     * 将object转换成json字符串
     *
     * @param object
     *
     * @return json字符串
     */
    public static final String objectToJsonString(Object object) {
        String jsonString = JSON.toJSONString(object);
        return jsonString;
    }

    /**
     * 将object转换成格式化的json字符串
     *
     * @param object
     *
     * @return json字符串
     */
    public static final String toPrettyJSONString(Object object) {
        return JSON.toJSONString(object, SerializerFeature.PrettyFormat);
    }

    /**
     * 将object转换成json字符串,可以指定序列化的特征
     *
     * @param object
     * @param features
     *
     * @return json字符串
     */
    public static final String toJSONString(Object object, SerializerFeature... features) {
        return JSON.toJSONString(object, features);
    }

    /**
     * 将object转换成json字符串,可以指定序列化的特征及日期格式
     *
     * @param object
     * @param dateFormat
     * @param features
     *
     * @return json字符串
     */
    public static final String toJSONStringWithDateFormat(Object object, String dateFormat,
                                                          SerializerFeature... features) {
        return JSON.toJSONStringWithDateFormat(object, dateFormat, features);
    }

    /**
     * 通过反射,将string转换成javabean
     *
     * @param text
     * @param clazz
     * @param features
     * @param <T>
     *
     * @return javabean
     */
    public static final <T> T parseObject(String text, Class<T> clazz, Feature... features) {
        return JSON.parseObject(text, clazz, features);
    }

    /**
     * 将string 转换成javabean list
     *
     * @param text
     * @param clazz
     * @param <T>
     *
     * @return avabean list
     */
    public static final <T> List<T> parseList(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz);
    }

    /**
     * 不允许直接生成对象实例
     */
    private FastJsonUtil() {
    }


}
