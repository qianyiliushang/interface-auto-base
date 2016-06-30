package com.zombie.utils.base;

import com.zombie.base.BaseRequest;
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
     * 构建请求URL,需要在config.properties中配置访问的baseURL
     *
     * @param uri api路径
     *
     * @return url
     */
    @Deprecated
    public static URL builder(String uri) {
        URL url = null;
        try {
            url = new URL(ConfigUtil.get("baseURL") + uri);

        } catch (MalformedURLException e) {
            logger.error("build url error");
        }
        return url;
    }

    /**
     * 构建请求URL,需要在config.properties中配置访问的baseURL
     *
     * @param uri API短地址
     *
     * @return 请求地址
     */
    public static String defaultBuiler(String uri) {
        return ConfigUtil.get("baseURL" + uri);
    }

    /**
     * 构建请求URL,需要在config.properties中配置访问的baseURL
     *
     * @param uri    API短地址
     * @param params 追加在URL后面的参数,支持MAP,JAVABEAN
     *
     * @return 请求地址, 对params进行URLEncode
     */
    public static String defaultBuilder(String uri, Object params) {
        if (params == null) {
            return defaultBuiler(uri);
        }
        String paramsStr = ParamsBuilder.getFormData(params);
        return defaultBuiler(uri) + "?" + paramsStr;
    }


    /**
     * 构建请求URL
     *
     * @param url 完整的请求地址,包括api短地址
     *
     * @return 请求地址
     */
    public static String customBuiler(String url) {
        return url;
    }

    /**
     * 构建请求URL
     *
     * @param url    完整的请求地址,包括api短地址
     * @param params 追加在URL后面的参数,支持MAP,JAVABEAN
     *
     * @return 请求地址, 对params进行URLEncode
     */
    public static String customBuiler(String url, Object params) {
        if (params == null) {
            return customBuiler(url);
        }
        String paramsStr = ParamsBuilder.getFormData(params);
        return customBuiler(url) + "?" + paramsStr;
    }

    /**
     * 构建请求URL
     *
     * @param url    api访问地址,包括:http://host:port/context
     * @param uri    接口短地址
     * @param params 追加在URL后面的参数,支持MAP,JAVABEAN
     *
     * @return 请求地址, 对params进行URLEncode
     */
    public static String customBuiler(String url, String uri, Object params) {
        String baseUrl = url + uri;
        if (params == null) {
            return customBuiler(baseUrl);
        }
        String paramsStr = ParamsBuilder.getFormData(params);
        return customBuiler(baseUrl) + "?" + paramsStr;
    }

    public static String customBuiler(BaseRequest baseRequest) {
        String baseUrl = baseRequest.url();
        String paramStr = ParamsBuilder.getFormData(baseRequest);
        if (paramStr == null) {
            return customBuiler(baseUrl);
        }
        return customBuiler(baseUrl) + "?" + paramStr;
    }


}
