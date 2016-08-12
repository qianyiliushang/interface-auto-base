package com.zombie.httpclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zombie.base.BaseRequest;
import com.zombie.base.UploadRequest;
import com.zombie.utils.base.ParamsBuilder;
import com.zombie.utils.base.URLBuilder;
import com.zombie.utils.json.FastJsonUtil;
import com.zombie.utils.json.GsonUtils;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.RedirectLocations;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;

/**
 * HttpClient 4.5二次封装,方便接口测试
 */

public class HCHelper {
    private static Logger logger = LogManager.getLogger(HCHelper.class);
    private static final String CHARSET = "UTF-8";
    private static final int TIMEOUT = 15 * 1000;

    private static CloseableHttpClient getClient() {
        return HttpClients.createDefault();
    }

    private static CloseableHttpClient getCustomedClient() throws Exception {
        HttpClientBuilder builder = HttpClients.custom()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(SSLContexts.custom()
                                                                            .loadTrustMaterial(null, new TrustSelfSignedStrategy()).build()));
        return builder.build();
    }


    /**
     * POST FORM表单,所有参数都在表单中
     *
     * @param request 请求数据
     *
     * @return json Object
     */
    public static JSONObject postForm(BaseRequest request) {
        String url = request.url();
        HttpPost post = new HttpPost(url);
        post.setConfig(getCustromRequestConfig());
        logger.info("request url:\n{}", url);
        logger.info("request body is:\n{}", GsonUtils.parseJson(request));
        String requestStr = ParamsBuilder.getFormData(request);
        //logger.info("request form: {}", requestStr);
        StringEntity entity = new StringEntity(requestStr, CHARSET);
        entity.setContentType("application/x-www-form-urlencoded");
        post.setEntity(entity);
        return send(post);
    }

    /**
     * POST FROM表单,有部分参数在URL中
     *
     * @param request 请求数据
     * @param params  追加的URL后面的参数
     *
     * @return json Object
     */
    public static JSONObject postForm(BaseRequest request, Object params) {
        if (params == null) {
            return postForm(request);
        }
        String url = URLBuilder.customBuiler(request.url(), params);
        try {
            logger.info("request url:\n{}", URLDecoder.decode(url, CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpPost post = new HttpPost(url);
        post.setConfig(getCustromRequestConfig());
        logger.info("request body is:\n{}", GsonUtils.parseJson(request));
        String requestStr = ParamsBuilder.getFormData(request);
        StringEntity entity = new StringEntity(requestStr, CHARSET);
        entity.setContentType("application/x-www-form-urlencoded");
        post.setEntity(entity);
        return send(post);
    }


    /**
     * 发送json请求
     *
     * @param request 请求数据
     *
     * @return json object
     */
    public static JSONObject postJson(BaseRequest request) {
        String url = request.url();
        HttpPost post = new HttpPost(url);
        post.setConfig(getCustromRequestConfig());
        logger.info("request url:\n{}", url);
        String requestStr = GsonUtils.parseJson(request);
        logger.info("request body is:\n{}", requestStr);
        StringEntity entity = new StringEntity(requestStr, CHARSET);
        entity.setContentType("application/json;charset=utf-8");
        post.addHeader("Accept", "application/json");
        post.setEntity(entity);
        return send(post);
    }

    /**
     * 对于返回对象不是json的接口,调用此方法,直接返回字符串
     *
     * @param request 请求数据
     *
     * @return 响应body
     */
    public static String post(BaseRequest request) {
        String url = request.url();
        HttpPost post = new HttpPost(url);
        post.setConfig(getCustromRequestConfig());
        logger.info("request url:\n{}", url);
        logger.info("request body is:\n{}", GsonUtils.parseJson(request));
        String requestStr = ParamsBuilder.getFormData(request);
        //logger.info("request form: {}", requestStr);
        StringEntity entity = new StringEntity(requestStr, CHARSET);
        entity.setContentType("application/x-www-form-urlencoded");
        post.setEntity(entity);
        try {
            CloseableHttpClient client = getCustomedClient();
            CloseableHttpResponse response = client.execute(post);
            return EntityUtils.toString(response.getEntity(), CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    /**
     * 发送json请求
     *
     * @param request 请求数据
     * @param params  追加的URL后面的参数
     *
     * @return json object
     */
    public static JSONObject postJson(BaseRequest request, Object params) {
        if (params == null) {
            return postJson(request);
        }
        String url = URLBuilder.customBuiler(request.url(), params);
        try {
            logger.info("request url:\n{}", URLDecoder.decode(url, CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpPost post = new HttpPost(url);
        post.setConfig(getCustromRequestConfig());
        logger.info("request url:\n{}", url);
        String requestStr = GsonUtils.parseJson(request);
        logger.info("request body is:\n{}", requestStr);
        StringEntity entity = new StringEntity(requestStr, CHARSET);
        entity.setContentType("application/json;charset=utf-8");
        post.addHeader("Accept", "application/json");
        post.setEntity(entity);
        return send(post);

    }

    /**
     * 文件上传
     *
     * @param file    需要上传的文件
     * @param request 基本请求,包含文件上传地址,如果有其他参数需要同时提交,也放在request中<br>
     *                如果没有额外的参数,请使用{@link HCHelper#upload(File, String)}<br>
     *                此时,这些参数都追加在URL后面
     *
     * @return json object
     */
    public static JSONObject upload(File file, BaseRequest request) {
        String url = request.url();
        String requestStr = GsonUtils.parseJson(request);
        if (!requestStr.equals("{}")) {
            url = URLBuilder.customBuiler(url, request);
        }
        HttpPost post = new HttpPost(url);
        post.setConfig(getCustromRequestConfig());
        logger.info("request url:\n{}", url);
        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addPart("file", new FileBody(file, ContentType.APPLICATION_OCTET_STREAM, file.getName())).build();
        post.setEntity(httpEntity);
        return send(post);
    }

    /**
     * 文件上传,仅仅是上传文件,没有其他参数
     *
     * @param file 需要上传的文件
     * @param url  文件上传地址
     *
     * @return json Object
     */
    public static JSONObject upload(File file, String url) {
        HttpPost post = new HttpPost(url);
        post.setConfig(getCustromRequestConfig());
        logger.info("request url:\n{}", url);
        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addPart("file", new FileBody(file, ContentType.APPLICATION_OCTET_STREAM, file.getName())).build();
        post.setEntity(httpEntity);
        return send(post);
    }

    /**
     * 多文件上传
     *
     * @param files 待上传文件
     * @param url   上传地址
     *
     * @return json Object
     */
    public static JSONObject upload(File[] files, String url) {
        HttpPost post = new HttpPost(url);
        post.setConfig(getCustromRequestConfig());
        logger.info("request url:\n{}", url);
        HttpEntity entity = null;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        for (int i = 0; i < files.length; i++) {
            FileBody fileBody = new FileBody(files[i], ContentType.APPLICATION_OCTET_STREAM, files[i].getName());
            builder.addPart("file" + i, fileBody).build();
        }
        post.setEntity(entity);
        return send(post);
    }

    /**
     * 通用文件下载
     *
     * @param url      下载地址
     * @param savePath 文件保存路径,如果为空,默认保存在当前项目跟目录下的build/downloads目录
     *
     * @return 包含下载文件绝对路径的json object<br>
     * {
     * "downloadFilePath":"文件保存路径",
     * "httpcode":"http状态码",
     * "file":"下载下来的文件"
     * }
     */
    public static JSONObject download(String url, String savePath) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(getCustromRequestConfig());
        return mediaDownload(httpGet, savePath);

    }

    /**
     * 通用文件下载,默认保存在当前项目跟目录下的build/downloads目录
     *
     * @param url 下载地址
     *
     * @return 包含下载文件绝对路径的json object<br>
     * {
     * "downloadFilePath":"文件保存路径",
     * "httpcode":"http状态码",
     * "file":"下载下来的文件"
     * }
     */
    public static JSONObject download(String url) {
        return download(url, null);
    }

    /**
     * 通用文件下载
     *
     * @param url      下载地址
     * @param params   追加在下载地址后面的参数
     * @param savePath 文件保存路径,如果为空,默认保存在当前项目跟目录下的build/downloads目录
     *
     * @return 包含下载文件绝对路径的json object<br>
     * {
     * "downloadFilePath":"文件保存路径",
     * "httpcode":"http状态码",
     * "file":"下载下来的文件"
     * }
     */
    public static JSONObject download(String url, Object params, String savePath) {
        String downloadUrl = URLBuilder.customBuiler(url, params);
        return download(downloadUrl, savePath);
    }

    /**
     * 通用文件下载,默认保存在当前项目跟目录下的build/downloads目录
     *
     * @param url    下载地址
     * @param params 追加在下载地址后面的参数
     *
     * @return 包含下载文件绝对路径的json object<br>
     * {
     * "downloadFilePath":"文件保存路径",
     * "httpcode":"http状态码",
     * "file":"下载下来的文件"
     * }
     */
    public static JSONObject download(String url, Object params) {
        String downloadUrl = URLBuilder.customBuiler(url, params);
        return download(downloadUrl);
    }

    /**
     * 通用文件下载,默认保存在当前项目跟目录下的build/downloads目录
     *
     * @param request 包含请求数据及请求地址,请求数据可以为空,此时等价于{@link HCHelper#download(String)}<br>
     *                当request中包含请求参数时,等价于{@link HCHelper#download(String, Object)}
     *
     * @return 包含下载文件绝对路径的json object<br>
     * {
     * "downloadFilePath":"文件保存路径",
     * "httpcode":"http状态码",
     * "file":"下载下来的文件"
     * }
     */
    public static JSONObject download(BaseRequest request) {
        String url = request.url();
        return download(url, request, null);
    }

    /**
     * 通用文件下载
     *
     * @param request  包含请求数据及请求地址,请求数据可以为空,此时等价于{@link HCHelper#download(String, String)}<br>
     *                 当request中包含请求参数时,等价于{@link HCHelper#download(String, Object, String)}
     * @param savePath 文件保存路径,如果为空,默认保存在当前项目跟目录下的build/downloads目录
     *
     * @return 包含下载文件绝对路径的json object<br>
     * {
     * "downloadFilePath":"文件保存路径",
     * "httpcode":"http状态码",
     * "file":"下载下来的文件"
     * }
     */
    public static JSONObject download(BaseRequest request, String savePath) {
        String url = request.url();
        return download(url, request, savePath);
    }

    /**
     * HTTP GET请求
     *
     * @param request 请求内容
     *
     * @return json Object
     */
    public static JSONObject doGet(BaseRequest request) {
        String url = URLBuilder.customBuiler(request);
        try {
            logger.info("request url:\n{}", URLDecoder.decode(url, CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(getCustromRequestConfig());
        return send(httpGet);

    }

    /**
     * 发送构建好的请求
     *
     * @param request 具体的请求,HttpPost,HttpGet等等
     *
     * @return json格式的响应信息, 如果http报错, 直接返回HTTP STATUS CODE及 ERROR STACKTRACE
     */
    private static JSONObject send(HttpUriRequest request) {
        CloseableHttpClient client = null;
        try {
            client = getCustomedClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);
            JSONObject jsonObject = new JSONObject();
            StatusLine statusLine = response.getStatusLine();
            //logger.info("status line:\n{}", FastJsonUtil.toPrettyJSONString(statusLine));
            //  logger.info("heads:\n{}", FastJsonUtil.toPrettyJSONString(headers));
            int statusCode = statusLine.getStatusCode();
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, CHARSET);
            if (statusCode >= 200 && statusCode < 300) {
                jsonObject = JSON.parseObject(result);
            } else {
                jsonObject.put("errorCode", statusCode);
                jsonObject.put("errorMessage", response.getStatusLine().getReasonPhrase());
                jsonObject.put("stackTrace", result);
            }
            logger.info("response from server:\n{}", FastJsonUtil.toPrettyJSONString(jsonObject));
            EntityUtils.consume(entity);
            return jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static RequestConfig getCustromRequestConfig() {
        RequestConfig.Builder builder = RequestConfig.custom();
        builder.setConnectionRequestTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT)
                .setConnectTimeout(TIMEOUT);
        RequestConfig requestConfig = builder.build();
        return requestConfig;
    }

    private static JSONObject mediaDownload(HttpUriRequest request, String savePath) {
        String url = request.getURI().toString();
        CloseableHttpClient client = null;
        try {
            client = getCustomedClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
        CloseableHttpResponse response = null;
        HttpContext context = new BasicHttpContext();
        if (savePath == null || savePath.equals("")) {
            String classpath = System.getProperty("user.dir");
            savePath = classpath + "/" + "build/" + "downloads";
        }
        try {
            response = client.execute(request);
            //下载链接跳转
            RedirectLocations locations = (RedirectLocations) context.getAttribute(HttpClientContext.REDIRECT_LOCATIONS);
            if (locations != null) {
                URI downloadUrl = locations.getAll().get(0);
                String filename = downloadUrl.toURL().getFile();
                logger.info("downloadUrl=" + downloadUrl);


                File downloadFile = new File(savePath + File.separator + filename);
                byte[] fileByte = EntityUtils.toByteArray(response.getEntity());
                FileUtils.writeByteArrayToFile(downloadFile, fileByte);
                JSONObject obj = new JSONObject();
                obj.put("downloadFilePath", downloadFile.getAbsolutePath());
                obj.put("httpcode", response.getStatusLine().getStatusCode());
                obj.put("file", fileByte);
                logger.info("文件下载成功,保存路径为:{}", downloadFile.getAbsolutePath());


                return obj;
            } else {
                if (response.getStatusLine().getStatusCode() != 200) {

                    System.out.println("request url failed, http code=" + response.getStatusLine().getStatusCode()
                                               + ", url=" + url);

                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        String resultStr = EntityUtils.toString(entity, "utf-8");
                        JSONObject result = JSON.parseObject(resultStr);
                        logger.info("response is \n{}", result.toJSONString());
                        return result;
                    }
                } else {
                    HttpEntity entity = response.getEntity();
                    String[] strs = url.split("/");
                    String fileName = strs[strs.length - 1];
                    File downloadFile = new File(savePath + File.separator + fileName);
                    byte[] fileByte = EntityUtils.toByteArray(response.getEntity());
                    FileUtils.writeByteArrayToFile(downloadFile, fileByte);
                    JSONObject obj = new JSONObject();
                    obj.put("downloadFilePath", downloadFile.getAbsolutePath());
                    obj.put("httpcode", response.getStatusLine().getStatusCode());
                    obj.put("file", fileByte);
                    logger.info("文件下载成功,保存路径为:{}", downloadFile.getAbsolutePath());
                    return obj;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("request url=" + url + ", exception, msg=" + e.getMessage());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;

    }


    public static void main(String[] args) {
       /* WechatLoginRequest request = new WechatLoginRequest();
        request.setOpenid("autoTest1");
        JSONObject jsonObject = HCHelper.postJson(request);
        logger.info("base response: {}", jsonObject.getJSONObject("baseResponse").toString());*/

        UploadRequest uploadRequest = new UploadRequest();
        String request = GsonUtils.parseJson(uploadRequest);
        request = ParamsBuilder.getFormData(uploadRequest);
        System.out.println(request);
       /* String classpathRoot = System.getProperty("user.dir");
        File file = new File(classpathRoot, "test.jpg");
        System.out.println(request.equals("{}"));*/
        //  JSONObject jsonObject = upload(file, uploadRequest);
        //  HCHelper.upload(file, "http://139.196.189.53:8080/imgsvc/api/pb/v1/img/upload/op");

       /* String fileUrl = "http://139.196.189.53:808/imgs/headimg/201606/a4/a431f740-3dfc-4b95-b5a1-36c2abb7eb87.jpg";
        String downloadPath = classpathRoot + "imgs";
        HCHelper.download(fileUrl, "");*/

    }
}
