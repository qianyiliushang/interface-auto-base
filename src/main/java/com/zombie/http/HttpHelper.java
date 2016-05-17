package com.zombie.http;

import com.google.gson.JsonSyntaxException;
import com.zombie.utils.base.ParamsBuilder;
import com.zombie.utils.date.DateUtils;
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
     * 提交form表单
     *
     * @param request 表单内容,可以为json String, javabean或者map
     * @param uri     接口地址,不包括服务器IP,端口,上下文,需要在config.properties中配置BaseURL
     * @param params  追加在url后面的参数
     *
     * @return json字符串
     */
    public static String doFormDataPost(Object request, String uri, Object params) {
        if (params == null) {
            return doFormDataPost(request, uri);
        }
        HttpURLConnection connection = HttpURLConnectionFactory.getConnectionWithParams(uri, params);
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
        HttpURLConnection connection = HttpURLConnectionFactory.jsonPostConnectionWithParams(uri, params);
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

    public static String doUpload(String uri, Object params, String filePath) {
        HttpURLConnection urlConnection = HttpURLConnectionFactory.uploadConnection(uri, params);
        return postMultipartForm(filePath, urlConnection);
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
        String response = "";
        try {
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            logger.error("IOException:{}", e.getMessage());
        }
        byte[] inputBytes = readStream(inputStream);
        String jsonResponse = null;
        try {
            jsonResponse = new String(inputBytes, "UTF-8");
            response = jsonResponse;
            jsonResponse = GsonUtils.formatJsonString(jsonResponse);

        } catch (UnsupportedEncodingException e) {
            logger.error("encoding error:{}", e.getMessage());
        } catch (JsonSyntaxException e) {
            logger.error("parse json object error, raw response is:\n{}", response);
            //throw new ApiException("200", response);
        }
        logger.info("response from server:\n{}", jsonResponse);
        return jsonResponse;

    }

    private static String getResponse(HttpURLConnection connection) {
        String response;
        try {
            InputStream inputStream = connection.getInputStream();
            int ch;
            StringBuffer sb = new StringBuffer();
            while ((ch = inputStream.read()) != -1) {
                sb.append((char) ch);
            }
            response = sb.toString();
            logger.info("response from server:\n{}", response);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String errorRespose(HttpURLConnection connection) {
       /* String errorResponse = null;
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
        logger.info("response from server:\n{}", errorResponse);
        return errorResponse;*/
        InputStream inputStream = null;
        String errorResponse = null;
        try {
            inputStream = connection.getInputStream();
            byte[] inputBytes = readStream(inputStream);
            errorResponse = new String(inputBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("encoding error:{}", e.getMessage());
        } catch (IOException e) {
            logger.error("IOException while get input stream from connection:\n{}", e.getMessage());
        } catch (JsonSyntaxException e) {
            logger.error("parse json object error, raw response is:\n{}", errorResponse);
        }
        logger.info("response from server:\n{}", errorResponse);
        return errorResponse;
    }

    private static String basePost(Object request, HttpURLConnection connection) {

        DataOutputStream outputStream = null;
        String jsonResponse = null;
        String jsonRequest = GsonUtils.parseJson(request);
        logger.info("Content-Type:{}", connection.getRequestProperty("Content-Type"));
        if (connection.getRequestProperty("Content-Type") == null) {
            jsonRequest = ParamsBuilder.getFormData(request);
        }
        logger.info("request params:\n{}", jsonRequest);
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
        //  logger.info("response is:\n{}", jsonResponse);

        return jsonResponse;

    }

    public static File doGetWithFileResponse(String uri, String format) {
        HttpURLConnection connection = HttpURLConnectionFactory.basicGetConnection(uri);
        File file = null;
        InputStream inputStream = null;
        try {
            if (connection.getResponseCode() == 200) {
                File rootClassPath = new File(System.getProperty("user.dir"));
                File fileSavePath = new File(rootClassPath + "/build/files");
                // logger.info("生成的文件保存在:{}", fileSavePath.getAbsolutePath());
                if (!fileSavePath.exists()) {
                    fileSavePath.mkdirs();
                }
                inputStream = connection.getInputStream();
                String fileName = DateUtils.getCurrentTime("yyyyMMddHHmmss") + "." + format;
                file = new File(fileSavePath + "/" + fileName);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] inputBytes = new byte[1024];
                int byteCount = 0;
                // int bytesWritten = 0;
                while ((byteCount = inputStream.read(inputBytes)) != -1) {
                    //fos.write(inputBytes);
                    fos.write(inputBytes, 0, byteCount);
                    //  bytesWritten += byteCount;
                }
                inputStream.close();
                fos.close();
                logger.info("文件保存成功,路径为:{}", file.getAbsolutePath());
                return file;

            } else {
                errorRespose(connection);
                return file;
            }
        } catch (IOException e) {
            logger.error("IOEXCEPTION:{}", e.getMessage());
        }
        return null;
    }

    public static File doGetWithFileResponse(String uri, Object params, String format) {
        if (params == null) {
            return doGetWithFileResponse(uri, format);
        }
        HttpURLConnection connection = HttpURLConnectionFactory.getConnectionWithParams(uri, params);
        File file = null;
        InputStream inputStream = null;
        try {
            if (connection.getResponseCode() == 200) {
                File rootClassPath = new File(System.getProperty("user.dir"));
                File fileSavePath = new File(rootClassPath + "/build/files");
                if (!fileSavePath.exists()) {
                    fileSavePath.mkdirs();
                }
                inputStream = connection.getInputStream();
                String fileName = DateUtils.getCurrentTime("yyyyMMddHHmmss") + "." + format;
                file = new File(fileSavePath + "/" + fileName);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] inputBytes = new byte[1024];
                int byteCount = 0;
                // int bytesWritten = 0;
                while ((byteCount = inputStream.read(inputBytes)) != -1) {
                    fos.write(inputBytes, 0, byteCount);
                    // bytesWritten += byteCount;
                }
                inputStream.close();
                fos.close();
                logger.info("文件保存成功,文件为:{}", file.getAbsolutePath());
                return file;

            } else {
                errorRespose(connection);
                return file;
            }
        } catch (IOException e) {
            logger.error("IOEXCEPTION:{}", e.getMessage());
        }
        return null;
    }

    public static File doGetWithFileResponse(String uri, Object params, String path, String format) {
        if (path == "" || path == null) {
            return doGetWithFileResponse(uri, params, format);
        }
        HttpURLConnection connection = HttpURLConnectionFactory.getConnectionWithParams(uri, params);
        File file = null;
        InputStream inputStream = null;
        try {
            if (connection.getResponseCode() == 200) {

                File fileSavePath = new File(path);
                // logger.info("生成的文件保存在:{}", fileSavePath.getAbsolutePath());

                if (!fileSavePath.exists()) {
                    fileSavePath.mkdirs();
                }
                inputStream = connection.getInputStream();
                String fileName = DateUtils.getCurrentTime("yyyyMMddHHmmss") + "." + format;
                file = new File(fileSavePath + "/" + fileName);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] inputBytes = new byte[1024];
                int byteCount = 0;
                //int bytesWritten = 0;
                while ((byteCount = inputStream.read(inputBytes)) != -1) {
                    // fos.write(inputBytes);
                    fos.write(inputBytes, 0, byteCount);
                    //bytesWritten += byteCount;
                }
                inputStream.close();
                fos.close();
                logger.info("文件保存成功,文件为:{}", file.getAbsolutePath());
                return file;

            } else {
                errorRespose(connection);
                return file;
            }
        } catch (IOException e) {
            logger.error("IOEXCEPTION:{}", e.getMessage());
        }
        return null;
    }

/*    private static File fileResponse(HttpURLConnection httpURLConnection, String path, String format) {
        InputStream inputStream = null;
        File file = null;
        File rootClassPath = new File(System.getProperty("user.dir"));
        File fileSavePath = null;
        if (path == null || path.equals("")) {
            fileSavePath = new File(rootClassPath + "/build/files");
            // logger.info("未指定文件保存目录,使用默认的目录:{}", fileSavePath.getAbsolutePath());
        } else {
            fileSavePath = new File(path);
            // logger.info("生成的文件保存在:{}", fileSavePath.getAbsolutePath());
        }
        if (!fileSavePath.exists()) {
            fileSavePath.mkdirs();
        }

        try {
            inputStream = httpURLConnection.getInputStream();
            String fileName = DateUtils.getCurrentTime("yyyyMMddHHmmss") + format;
            file = new File(fileSavePath + fileName);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] inputBytes = new byte[1024];
            int byteCount = 0;
            int bytesWritten = 0;
            while ((byteCount = inputStream.read(inputBytes)) != -1) {
                fos.write(inputBytes, bytesWritten, byteCount);
                bytesWritten += byteCount;
            }
            inputStream.close();
            fos.close();
            return file;

        } catch (IOException e) {
            logger.error("IOEXCEPTION:{}", e.getMessage());
            return file;
        }

    }*/

    private static String postMultipartForm(String path, HttpURLConnection connection) {
        String end = "\r\n";
        String twoHyphens = "--";
        int bytesRead, bytesAvailable, bufferSize;
        int maxBufferSize = 1 * 1024 * 1024;
        File file = new File(path);
        String fileName = file.getName();
        String boundary = "=====AutoTest=====";
        String jsonResponse = null;
        InputStream inputStream = null;
        try {
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + end);
            outputStream.writeBytes("Content-Disposition: form-data; " + "name=\"uploadFile\";filename=" +
                                            "\"" + fileName + "\"" + end);
            outputStream.writeBytes("Content-Type:image/jpeg" + end);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + end);
            outputStream.writeBytes(end);
            FileInputStream fileInputStream = new FileInputStream(file);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            outputStream.writeBytes(end);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + end);
            inputStream = connection.getInputStream();
            jsonResponse = getResponse(connection);

            fileInputStream.close();
            inputStream.close();
            outputStream.flush();
            outputStream.close();

            logger.info("response status code is {}", connection.getResponseCode());
          /*  if (connection.getResponseCode() == 200) {
                jsonResponse = successResponse(connection);
            } else {
                jsonResponse = errorRespose(connection);
            }*/

        } catch (IOException e) {
            e.printStackTrace();
            jsonResponse = getResponse(connection);
        }

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
