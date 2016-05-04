package com.zombie.utils.report;

import com.alibaba.fastjson.JSONObject;
import com.jayway.restassured.path.json.JsonPath;
import ru.yandex.qatools.allure.annotations.Attachment;

/**
 * 当使用allure-framework作为测试报告生成框架时,本类中的方法可以将附件附加到测试结果中
 */

public class AttachmentUtils {
    @Attachment
    public static String attachText(String text) {
        return text;
    }

    @Attachment
    public static String attachText(JsonPath jsonPath) {
        return jsonPath.prettify();
    }

    @Attachment
    public static String attachText(JSONObject jsonObject) {
        return jsonObject.toJSONString();
    }

    private AttachmentUtils() {
    }
}
