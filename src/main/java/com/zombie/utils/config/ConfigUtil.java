package com.zombie.utils.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 配置文件相关的工具类
 */

public class ConfigUtil {
    private static final Logger logger = LogManager.getLogger(ConfigUtil.class);
    private static String filePath = System.getProperty("user.dir") + "/src/main/resources/config.properties";


    /**
     * 从config.properties文件中读取配置文件
     *
     * @param key
     *
     * @return key对应的value
     */
    public static String get(String key) {
        try {

            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(filePath));
            Properties properties = new Properties();

            properties.load(inputStreamReader);
            Map<String, String> propMap = new HashMap<String, String>();

            Set<Object> keySet = properties.keySet();
            for (Object object : keySet) {
                String propKey = object.toString().trim();
                String propValue = properties.getProperty(propKey).toString().trim();
                propMap.put(propKey, propValue);
            }

            if (propMap.containsKey(key.trim())) {
                return propMap.get(key);
            } else {
                logger.error("配置文件中没有" + key);
                return null;
            }
        } catch (FileNotFoundException e) {
            logger.error("config.properties文件不存在,请检查");
            return null;
        } catch (IOException e) {
            logger.error("加载配置文件出错");
            return null;
        }

    }

    public static void update(String key, String value) {
        try {
            Properties properties = new Properties();
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(filePath));
            properties.load(inputStreamReader);
            logger.info("origin " + key + " is:" + properties.get(key));
            //String filePath = ConfigUtil.class.getClassLoader().getResource("config.properties").getFile();
            OutputStream fileOutputStream = new FileOutputStream(filePath);
            properties.setProperty(key, value);
            properties.store(fileOutputStream, "update");
            fileOutputStream.close();
            logger.info("latest " + key + " is:" + properties.get(key));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        System.out.println(get("baseURL"));
        update("name", "zombie");

    }
}
