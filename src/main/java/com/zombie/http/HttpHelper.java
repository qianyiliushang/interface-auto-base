package com.zombie.http;

import com.zombie.utils.base.ParamsBuilder;
import com.zombie.utils.base.URLBuilder;
import com.zombie.utils.json.GsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 使用HttpUrlConnection发送各种请求
 */
public class HttpHelper {
    private static Logger logger = LoggerFactory.getLogger(HttpHelper.class);


    /**
     * 发送json格式的请求,并返回json格式的String
     *
     * @param request 请求的body体,可以是javaBean,或者直接是json字符串
     * @param uri     接口地址,不包括服务器IP,端口,上下文,需要在config.properties中配置BaseURL
     *
     * @return json字符串
     */
    public static String doJsonPost(Object request, String uri) {
        DataOutputStream outputStream = null;
        String jsonResponse = null;
        String jsonRequest = GsonUtils.parseJson(request);

        URL url = URLBuilder.builder(uri);
        logger.info("request url:{}", url);
        logger.info("request params:\n{}", jsonRequest);

        try {
            HttpURLConnection connection = HttpURLConnectionFactory.jsonConnection(url);
            byte[] bs = jsonRequest.getBytes();
            connection.setRequestProperty("Content-Length", String.valueOf(bs.length));
            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.write(bs);
            outputStream.flush();

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

    public static String postFormData(Object request, String uri) {
        DataOutputStream outputStream = null;
        String jsonResponse = null;
        String jsonRequest = GsonUtils.parseJson(request);

        URL url = URLBuilder.builder(uri);
        logger.info("request url:{}", url);
        logger.info("request params:\n{}", jsonRequest);
        String param = ParamsBuilder.getFormData(request);
        byte[] bs = param.getBytes();
        HttpURLConnection connection = HttpURLConnectionFactory.getBasicConnection(url);
        connection.setRequestProperty("Content-Length", String.valueOf(bs.length));
        try {
            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.write(bs);
            outputStream.flush();
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
                // outStream.close();
                // inStream.close();
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
}
