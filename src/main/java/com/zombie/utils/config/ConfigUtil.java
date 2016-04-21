package com.zombie.utils.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 配置文件相关的工具类
 */

public class ConfigUtil {
    private static final Logger logger = LogManager.getLogger(ConfigUtil.class);
    private static Map<String, String> propMap = new HashMap<String, String>();

    /**
     * 从config.properties文件中读取配置文件
     *
     * @param key
     *
     * @return key对应的value
     */
    public static String get(String key) {
        if (propMap.containsKey(key.trim())) {
            return propMap.get(key);
        } else {
            logger.error("配置文件中没有" + key);
            return null;
        }
    }


    static {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties"), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            logger.error("配置文件编码不正确，请检查");
            e.printStackTrace();
        }

        Properties properties = new Properties();

        if (inputStreamReader != null) {
            try {
                properties.load(inputStreamReader);
                Set<Object> keySet = properties.keySet();
                for (Object object : keySet) {
                    String propKey = object.toString().trim();
                    String propValue = properties.getProperty(propKey).toString().trim();
                    propMap.put(propKey, propValue);
                }
            } catch (IOException e) {
                logger.error("配置文件不存在，请检查");
                e.printStackTrace();
            } finally {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    logger.error("关闭配置文件出错");
                }
            }


        }
    }

}
