package com.zombie.http;

import com.zombie.utils.base.ParamsBuilder;
import com.zombie.utils.base.URLBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 *
 */

public class HttpURLConnectionFactory {
    private static Logger logger = LoggerFactory.getLogger(HttpURLConnectionFactory.class);

    /**
     * 获取基础HTPPCONNECTION,适用于所有的请求数据都在body体中
     *
     * @param uri 接口地址,不包括域名(ip)端口号及上下文,需要在config.properties中配置BaseURL
     *
     * @return
     */
    public static HttpURLConnection basicPostConnection(String uri) {
        URL url = URLBuilder.builder(uri);
        logger.info("request url:{}", url);
        HttpURLConnection connection = basicConnection(url);
        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 获取httpconnection,并将一些需要在url中传递的参数拼到url后,其它请求内容放在body体中
     *
     * @param uri    接口地址,不包括域名(ip)端口号及上下文,需要在config.properties中配置BaseURL
     * @param params 需要加在url后的参数,map或者javabean都可以
     *
     * @return
     */
    public static HttpURLConnection postConnectionWithparams(String uri, Object params) {
        URL url = ParamsBuilder.paramsBuilder(uri, params);
        logger.info("request url:{}", url);
        HttpURLConnection connection = basicConnection(url);
        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static HttpURLConnection postConncetionWithoutConfig(String baseUrl, String uri, Object params) {
        URL url = ParamsBuilder.paramsBuilderWithoutConfig(baseUrl, uri, params);
        HttpURLConnection connection = basicConnection(url);
        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 设置content-type为application/json,适合request body为json格式的请求
     *
     * @param uri 接口地址,不包括域名(ip)端口号及上下文,需要在config.properties中配置BaseURL
     *
     * @return
     */
    public static HttpURLConnection jsonPostConnection(String uri) {
        HttpURLConnection connection = basicPostConnection(uri);
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        return connection;
    }

    /**
     * 基础连接信息
     *
     * @param url 要打开的完整的URL,包含上下文,端口,短地址等等
     *
     * @return
     */
    public static HttpURLConnection basicConnection(URL url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setConnectTimeout(15 * 1000);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Accept-Language", "zh-CN");
        //connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Accept-Charset", "UTF-8");

        return connection;
    }

    /**
     * 获取connection,适用于没有请求参数的Get请求
     *
     * @param uri 接口地址,不包括域名(ip)端口号及上下文,需要在config.properties中配置BaseURL
     *
     * @return
     */
    public static HttpURLConnection basicGetConnection(String uri) {
        URL url = ParamsBuilder.paramsBuilder(uri);
        HttpURLConnection connection = basicConnection(url);
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        return connection;
    }

    /**
     * 获取connection
     *
     * @param uri    uri 接口地址,不包括域名(ip)端口号及上下文,需要在config.properties中配置BaseURL
     * @param params 加在url后面的请求参数,支持map和javabean,其它格式未做测试
     *
     * @return
     */
    public static HttpURLConnection getConnectionWithParams(String uri, Object params) {
        if (params == null) {
            return basicGetConnection(uri);
        }
        URL url = ParamsBuilder.paramsBuilder(uri, params);
        HttpURLConnection connection = basicConnection(url);
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static HttpURLConnection getConnectionWithoutBaseUrl(String baseUrl, Object params) {
        URL url = ParamsBuilder.paramsBuilderWithoutConfig(baseUrl, params);
        HttpURLConnection connection = basicConnection(url);
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static HttpURLConnection getConnectionWithoutBaseUrl(String baseUrl, String uri, Object params) {
        if (uri == null) {
            return getConnectionWithoutBaseUrl(baseUrl, params);
        }
        URL url = ParamsBuilder.paramsBuilderWithoutConfig(baseUrl, uri, params);
        HttpURLConnection connection = basicConnection(url);
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        return connection;
    }


}
