package com.zombie.http;

import com.zombie.utils.base.URLBuilder;
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


    public static String doJsonPost(Object request, String uri) {
        DataOutputStream outputStream = null;
        String jsonResponse = null;
        // String jsonRequest = FastJsonUtil.toPrettyJSONString(request);
        String jsonRequest = request.toString();
        URL url = URLBuilder.builder(uri);
        logger.info("request url:{}", url);
        logger.info("request params:{}", jsonRequest);

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(15 * 1000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Accept-Language", "zh-CN");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
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
        logger.info("response is:{}", jsonResponse);

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


/*
    private static String bytesToJsonResoponse(byte[] bytes) {
        String jsonResponse = null;
        try {
            jsonResponse = new String(bytes, "UTF-8");
            jsonResponse = FastJsonUtil.toPrettyJSONString(jsonResponse);

        } catch (UnsupportedEncodingException e) {
            logger.error("encoding error:{}", e.getMessage());
        }

        return jsonResponse;
    }
*/

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
            //jsonResponse = FastJsonUtil.toPrettyJSONString(jsonResponse);

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
