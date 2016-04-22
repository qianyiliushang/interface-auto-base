package com.zombie.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 *
 */

public class HttpURLConnectionFactory {

    /**
     * 获取基础HTPPCONNECTION
     *
     * @param url 需要打开的链接地址
     *
     * @return
     */
    public static HttpURLConnection getBasicConnection(URL url) {
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
     * @param url 需要打开的链接地址
     *
     * @return
     */
    public static HttpURLConnection jsonConnection(URL url) {
        HttpURLConnection connection = getBasicConnection(url);
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        return connection;
    }

}
