package com.zombie.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zombie.utils.base.ParamsBuilder;
import com.zombie.utils.json.FastJsonUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 *
 */

public class HCUtils {
    private static Logger logger = LogManager.getLogger(HCUtils.class);

    public static JSONObject uploadMedia(String url, File file) {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
        httpPost.setConfig(requestConfig);

        HttpEntity requestEntity = MultipartEntityBuilder.create().addPart("media",
                                                                           new FileBody(file, ContentType.APPLICATION_OCTET_STREAM, file.getName())).build();
        httpPost.setEntity(requestEntity);

        try {
            response = httpClient.execute(httpPost, new BasicHttpContext());

            if (response.getStatusLine().getStatusCode() != 200) {

                logger.error("请求发送失败,URL:\n{}\n http code: {}", url, response.getStatusLine().getStatusCode());

               /* System.out.println("request url failed, http code=" + response.getStatusLine().getStatusCode()
                                           + ", url=" + url);*/
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String resultStr = EntityUtils.toString(entity, "utf-8");

                JSONObject result = JSON.parseObject(resultStr);
                logger.info("response is:\n{}", FastJsonUtil.toPrettyJSONString(result));
                /*if (result.getInteger("errcode") == 0) {
                    // 成功
                    //result.remove("errcode");
                    //result.remove("errmsg");
                    return result;
                } else {
                    System.out.println("request url=" + url + ",return value=");
                    System.out.println(resultStr);
                    int errCode = result.getInteger("errcode");
                    String errMsg = result.getString("errmsg");
                    throw new OApiException(errCode, errMsg);
                }*/

                return result;
            }
        } catch (IOException e) {
            // System.out.println("request url=" + url + ", exception, msg=" + e.getMessage());
            logger.error("请求url={}失败,异常信息为:{}", url, e.getMessage());
            e.printStackTrace();
        } finally {
            if (response != null)
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return null;
    }

    public static JSONObject uploadMedia(String uri, Object params, File file) {
        URL url = ParamsBuilder.paramsBuilder(uri, params);
        logger.info("request url is:{}", url);
        logger.info("request params:{}", FastJsonUtil.toPrettyJSONString(params));
        return uploadMedia(url.toString(), file);
    }
}
