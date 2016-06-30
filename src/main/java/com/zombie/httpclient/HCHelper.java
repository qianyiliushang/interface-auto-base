package com.zombie.httpclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zombie.base.BaseRequest;
import com.zombie.business.bean.Login;
import com.zombie.utils.base.ParamsBuilder;
import com.zombie.utils.base.URLBuilder;
import com.zombie.utils.json.FastJsonUtil;
import com.zombie.utils.json.GsonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * HTTP CLIENT UTILS FOR TESTING
 */

public class HCHelper {
    private static Logger logger = LogManager.getLogger(HCHelper.class);
    private static final String CHARSET = "UTF-8";
    private static final int TIMEOUT = 15 * 1000;

    private static CloseableHttpClient getClient() {
        return HttpClients.createDefault();
    }

    private static CloseableHttpClient getCustomedClient() {
        HttpClientBuilder builder = HttpClients.custom();
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
        logger.info("request form: {}", requestStr);
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
        CloseableHttpClient client = getClient();
        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);
            JSONObject jsonObject = new JSONObject();
            StatusLine statusLine = response.getStatusLine();
            //logger.info("status line:\n{}", FastJsonUtil.toPrettyJSONString(statusLine));
            //  logger.info("heads:\n{}", FastJsonUtil.toPrettyJSONString(headers));
            int statusCode = statusLine.getStatusCode();
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            if (statusCode >= 200 && statusCode < 300) {
                jsonObject = JSON.parseObject(result);
            } else {
                jsonObject.put("errorCode", statusCode);
                jsonObject.put("errorMessage", response.getStatusLine().getReasonPhrase());
                jsonObject.put("stackTrace", result);
            }
            logger.info("response from server:\n{}", FastJsonUtil.toPrettyJSONString(jsonObject));
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
                .setSocketTimeout(TIMEOUT);
        RequestConfig requestConfig = builder.build();
        return requestConfig;
    }

    public static void main(String[] args) {
        Login login = new Login();
        login.setChannel("测试");
        login.setDevice("android");
        System.out.println(URLBuilder.defaultBuilder("/api/pb/v1/register", login));
        // String uri = URLBuilder.builder("http://localhost:18080/web/api/pb/v1/register/wechat", login);
        // System.out.println(uri);
    }
}
