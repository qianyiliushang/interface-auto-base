package com.zombie.utils.base;

import com.zombie.utils.config.ConfigUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * url工具类
 */

public class URLBuilder {
    private static Logger logger = LogManager.getLogger(URLBuilder.class.getCanonicalName());

    /**
     * 构建post请求URL,需要在config.properties中配置访问的baseURL
     *
     * @param uri api路径
     *
     * @return url
     */
    public static URL builder(String uri) {
        URL url = null;
        try {
            url = new URL(ConfigUtil.get("baseURL") + uri);

        } catch (MalformedURLException e) {
            logger.error("build url error");
        }
        return url;
    }
}
