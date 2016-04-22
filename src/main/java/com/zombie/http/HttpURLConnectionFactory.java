package com.zombie.http;

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
     * 获取基础HTPPCONNECTION
     *
     * @param uri 接口地址,不包括域名(ip)端口号及上下文,需要在config.properties中配置BaseURL
     *
     * @return
     */
    public static HttpURLConnection postBasicConnection(String uri) {
        URL url = URLBuilder.builder(uri);
        logger.info("request url:{}", url);
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setConnectTimeout(15 * 1000);
        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Accept-Language", "zh-CN");
        //connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Accept-Charset", "UTF-8");

        return connection;
    }

    /**
     * 设置content-type为application/json,适合request body为json格式的请求
     *
     * @param uri 需要打开的链接地址
     *
     * @return
     */
    public static HttpURLConnection jsonPostConnection(String uri) {
        HttpURLConnection connection = postBasicConnection(uri);
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        return connection;
    }


    public static HttpURLConnection getBasicConnection(String uri) {
        HttpURLConnection connection = jsonPostConnection(uri);
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        return connection;
    }

}
