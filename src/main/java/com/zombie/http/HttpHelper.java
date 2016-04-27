package com.zombie.http;

import com.zombie.utils.base.ParamsBuilder;
import com.zombie.utils.json.GsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * 使用HttpUrlConnection发送各种请求
 */
public class HttpHelper {
    private static Logger logger = LoggerFactory.getLogger(HttpHelper.class);

    //不允许直接创建对象
    private HttpHelper() {
    }

    /**
     * 发送json格式的请求,并返回json格式的String
     *
     * @param request 请求的body体,可以是javaBean,或者直接是json字符串
     * @param uri     接口地址,不包括服务器IP,端口,上下文,需要在config.properties中配置BaseURL
     *
     * @return json字符串, 返回json String主要是为了在测试时处理比较方便,可以用jsonPath去解析,同时也可以转成相应的javabean去解析
     */
    public static String doJsonPost(Object request, String uri) {
        HttpURLConnection connection = HttpURLConnectionFactory.jsonPostConnection(uri);
        return basePost(request, connection);
    }

    /**
     * 以x-www-form-urlencoded形式发送Post表单
     *
     * @param request 表单内容,可以为json String, javabean或者map
     * @param uri     接口地址,不包括服务器IP,端口,上下文,需要在config.properties中配置BaseURL
     *
     * @return json字符串
     */
    public static String doFormDataPost(Object request, String uri) {
        HttpURLConnection connection = HttpURLConnectionFactory.basicPostConnection(uri);
        return basePost(request, connection);
    }

    /**
     * 发送json格式的请求,适用于请求URL后有要添加的参数,同时请求主体放在body体中,并返回json格式的String
     *
     * @param request 请求的body体,可以是javaBean,或者直接是json字符串
     * @param uri     接口地址,不包括服务器IP,端口,上下文,需要在config.properties中配置BaseURL
     * @param params  加在url后面的请求参数,支持map和javabean,其它格式未做测试
     *
     * @return Json String
     */
    public static String doJsonPost(Object request, String uri, Object params) {
        if (params == null) {
            return doJsonPost(request, uri);
        }
        HttpURLConnection connection = HttpURLConnectionFactory.postConnectionWithparams(uri, params);
        return basePost(request, connection);
    }

    public static String doPostWithoutConfig(String baseUrl, String uri, Object params, Object request) {
        HttpURLConnection connection = HttpURLConnectionFactory.postConncetionWithoutConfig(baseUrl, uri, params);
        return basePost(request, connection);
    }

    /**
     * 基本的Get请求(不带参数)
     *
     * @param uri 接口地址,不包括服务器IP,端口,上下文,需要在config.properties中配置BaseURL
     *
     * @return
     */
    public static String doGet(String uri) {
        HttpURLConnection connection = HttpURLConnectionFactory.basicGetConnection(uri);
        return baseGet(connection);
    }

    /**
     * @param params 追加在url后面的参数,支持map和javabean,其它格式未测试
     * @param uri    接口地址,不包括服务器IP,端口,上下文,需要在config.properties中配置BaseURL
     *
     * @return
     */
    public static String doGet(String uri, Object params) {
        if (params == null) {
            return doGet(uri);
        }
        HttpURLConnection connection = HttpURLConnectionFactory.getConnectionWithParams(uri, params);
        return baseGet(connection);
    }

    public static String doGetWithoutConfig(String url, String uri, Object params) {

        HttpURLConnection connection = HttpURLConnectionFactory.getConnectionWithoutBaseUrl(url, uri, params);
        return baseGet(connection);
    }

    /**
     * 将inputStrem转换为byte[]
     *
     * @param inStream
     *
     * @return byte[]
     */
    private static byte[] readStream(InputStream inStream) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            logger.error("IOException while read inputStream:{}", e.getMessage());
        }
        return outStream.toByteArray();
    }


    private static String successResponse(HttpURLConnection connection) {
        InputStream inputStream = null;
        try {
            inputStream = connection.getInputStream();


        } catch (IOException e) {
            logger.error("IOException:{}", e.getMessage());
        }
        byte[] inputBytes = readStream(inputStream);
        String jsonResponse = null;
        try {
            jsonResponse = new String(inputBytes, "UTF-8");
            jsonResponse = GsonUtils.formatJsonString(jsonResponse);

        } catch (UnsupportedEncodingException e) {
            logger.error("encoding error:{}", e.getMessage());
        }

        return jsonResponse;

    }

    private static String errorRespose(HttpURLConnection connection) {
        String errorResponse = null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        String line;
        StringBuffer errorMsg = new StringBuffer();
        try {
            if ((line = bufferedReader.readLine()) != null) {
                errorMsg.append(line).append("\n");
            }
            errorResponse = errorMsg.toString();

            bufferedReader.close();

        } catch (IOException e) {
            logger.error("IOException:{}", e.getMessage());
        }
        return errorResponse;
    }

    private static String basePost(Object request, HttpURLConnection connection) {

        DataOutputStream outputStream = null;
        String jsonResponse = null;
        String jsonRequest = GsonUtils.parseJson(request);
        logger.info("request params:\n{}", jsonRequest);
        logger.info("Content-Type:{}", connection.getRequestProperty("Content-Type"));
        if (connection.getRequestProperty("Content-Type") == null) {
            jsonRequest = ParamsBuilder.getFormData(request);
        }
        byte[] bs = jsonRequest.getBytes();
        connection.setRequestProperty("Content-Length", String.valueOf(bs.length));
        try {
            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.write(bs);
            outputStream.flush();
            logger.info("response status code is {}", connection.getResponseCode());
            if (connection.getResponseCode() == 200) {
                jsonResponse = successResponse(connection);
            } else {
                jsonResponse = errorRespose(connection);
            }
        } catch (IOException e) {
            logger.error("IOException:{}", e.getMessage());
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();

            } catch (IOException e) {
                logger.error("IOException:{}", e.getMessage());
            }
        }
        logger.info("response is:\n{}", jsonResponse);

        return jsonResponse;

    }

    private static String baseGet(HttpURLConnection connection) {
        String jsonResponse = null;
        try {
            if (connection.getResponseCode() == 200) {
                jsonResponse = successResponse(connection);
            } else {
                jsonResponse = errorRespose(connection);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }

}
