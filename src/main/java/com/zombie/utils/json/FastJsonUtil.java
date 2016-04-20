package com.zombie.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

/**
 * Created by chenpengpeng on 16/4/15.
 */
public class FastJsonUtil {

    public static final String objectToJsonString(Object object) {
        String jsonString = JSON.toJSONString(object);
        return jsonString;
    }


    public static final String toPrettyJSONString(Object object) {
        return JSON.toJSONString(object, SerializerFeature.PrettyFormat);
    }


    public static final String toJSONString(Object object, SerializerFeature... features) {
        return JSON.toJSONString(object, features);
    }

    public static final String toJSONStringWithDateFormat(Object object, String dateFormat,
                                                          SerializerFeature... features) {
        return JSON.toJSONStringWithDateFormat(object, dateFormat, features);
    }

    public static final <T> T parseObject(String text, Class<T> clazz, Feature... features) {
        return JSON.parseObject(text, clazz, features);
    }

    public static final <T> List<T> parseList(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz);
    }

    private FastJsonUtil() {
    }


}
