package com.zombie.utils.report;

import com.alibaba.fastjson.JSONObject;
import com.jayway.restassured.path.json.JsonPath;
import com.zombie.utils.base.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.annotations.Attachment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 当使用allure-framework作为测试报告生成框架时,本类中的方法可以将附件附加到测试结果中
 */

public class AttachmentUtils {
    private static Logger logger = LoggerFactory.getLogger(AttachmentUtils.class);

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

    @Attachment
    public static byte[] attachFile(File file) {
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes = StringUtils.readStream(inputStream);
            inputStream.close();
            return bytes;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Attachment
    public static byte[] attachFile(byte[] bytes) {
        return bytes;
    }
}
