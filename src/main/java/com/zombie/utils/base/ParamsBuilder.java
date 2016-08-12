package com.zombie.utils.base;

import com.zombie.business.bean.AccountConstant;
import com.zombie.business.bean.Content;
import com.zombie.utils.config.ConfigUtil;
import com.zombie.utils.json.FastJsonUtil;
import com.zombie.utils.json.GsonUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * 对于get请求或者需要在url后添加参数的post请求,构建请求URL
 */

public class ParamsBuilder {
    private static Logger logger = LoggerFactory.getLogger(ParamsBuilder.class);

    /**
     * 构建get请求url,或者post请求需要在url后追加参数的情景,需要事先在config.properties中配置好baseURL
     *
     * @param uri       接口地址,不包括服务器IP,端口,上下文,需要在config.properties中配置BaseURL
     * @param paramsMap 参数map
     *
     * @return url
     */
    public static URL paramsBuilder(String uri, Map<String, Object> paramsMap) {
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        String params = null;
        String destURL = ConfigUtil.get("baseURL") + uri;
        URL url = null;
        if (ValidateHelper.isNotEmptyMap(paramsMap)) {
            Iterator<Map.Entry<String, Object>> iterator = paramsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                if (entry.getValue() != null) {
                    nameValuePairList.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
                }
            }
            params = CommonUtils.format(nameValuePairList);

            destURL = destURL + "?" + params;

        }
        try {
            url = new URL(destURL);
        } catch (MalformedURLException e) {
            logger.error("构建URL失败,{}", e.getMessage());
        }
        return url;
    }

    /**
     * 对于没有在config.properties中配置baseUrl的情况,直接传入baseURL
     *
     * @param baseUrl 请求的基础URL,不包含上下文及接口地址
     * @param uri     接口地址
     * @param params  追加在URL后面的参数, 支持map及javabean
     *
     * @return
     */
    public static URL paramsBuilderWithoutConfig(String baseUrl, String uri, Object params) {
        if (baseUrl == null) {
            logger.error("baseUrl empty,forbidden");
            return null;
        }
        String destUrl = baseUrl;
        if (uri == null) {
            return paramsBuilderWithoutConfig(baseUrl, params);
        }
        destUrl += uri;
        if (params != null) {
            destUrl = destUrl + "?" + getFormData(params);
        }


        try {
            return new URL(destUrl);
        } catch (MalformedURLException e) {
            logger.error("构建URL失败,{}", e.getMessage());
            return null;
        }
    }

    /**
     * 对于没有在config.properties中配置baseUrl的情况,直接传入baseURL
     *
     * @param baseUrl 请求的基础URL,不包含上下文及接口地址
     * @param params  追加在URL后面的参数,支持map及javabean
     *
     * @return
     */
    public static URL paramsBuilderWithoutConfig(String baseUrl, Object params) {
        if (baseUrl == null) {
            logger.error("baseUrl empty,forbidden");
            return null;
        }

        String destUrl = baseUrl;
        if (params != null) {
            destUrl = destUrl + "?" + getFormData(params);
            logger.info("Request url:{}\n,Request params:{}", destUrl, GsonUtils.parseJson(params));
        }
        try {
            return new URL(destUrl);
        } catch (MalformedURLException e) {
            logger.error("构建URL失败,{}", e.getMessage());
            return null;
        }
    }


    /**
     * 对于没有在config.properties中配置baseUrl的情况,直接传入baseURL及接口地址
     *
     * @param baseUrl baseUrl 请求的基础URL,不包含上下文及接口地址
     * @param uri     接口地址
     *
     * @return
     */
    public static URL paramsBuilderWithoutConfig(String baseUrl, String uri) {
        String destUrl = baseUrl + uri;

        return paramsBuilderWithoutConfig(destUrl);
    }

    /**
     * 对于没有在config.properties中配置baseUrl的情况,直接传入baseURL
     *
     * @param baseUrl 请求的基础URL,不包含上下文及接口地址(或者完整的包含上下文及接口地址的URL)
     *
     * @return
     */
    public static URL paramsBuilderWithoutConfig(String baseUrl) {
        if (baseUrl == null) {
            logger.error("baseUrl empty,forbidden");
            return null;
        }
        try {
            return new URL(baseUrl);
        } catch (MalformedURLException e) {
            logger.error("构建URL失败,{}", e.getMessage());
            return null;
        }

    }

    /**
     * 构建get请求url,或者post请求需要在url后追加参数的情景,需要事先在config.properties中配置好baseURL
     *
     * @param uri    接口地址,不包括服务器IP,端口,上下文,需要在config.properties中配置BaseURL
     * @param params 参数
     *
     * @return
     */
    public static URL paramsBuilder(String uri, Object params) {
        //  Map<String, Object> formMap = GsonUtils.jsonObjectToMap(params);
        if (params == null) {
            return paramsBuilder(uri);
        }
        Map<String, Object> formMap = FastJsonUtil.objectToMap(params);
        return paramsBuilder(uri, formMap);
    }


    public static URL paramsBuilder(String uri) {
        String baseUrl = ConfigUtil.get("baseURL");
        try {
            if (uri != null) {

                baseUrl += uri;
            }
            return new URL(baseUrl);
        } catch (MalformedURLException e) {
            logger.error("构建URL失败,{}", e.getMessage());
            return null;
        }
    }

    /**
     * 将参数解析成key=value的形式,解析后的字符串可以直接追加在url后也可以通过post方式去发送(提交form表单)
     *
     * @param object 参数对象,支持map与javabean
     *
     * @return
     */
    public static String getFormData(Object object) {
        Map<String, Object> formMap = FastJsonUtil.objectToMap(object);
        //Map<String, Object> formMap = GsonUtils.jsonObjectToMap(object);
        final StringBuilder result = new StringBuilder();
        if (ValidateHelper.isNotEmptyMap(formMap)) {
            Iterator<Map.Entry<String, Object>> iterator = formMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                if (entry.getValue() != null) {
                    if (result.length() > 0) {
                        result.append("&");
                    }
                    //result.append(entry.getKey()).append("=").append(entry.getValue().toString());
                    try {
                        result.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                        //result.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
            }

        }

        return result.toString();
    }

    /**
     * 不允许直接生成对象
     */
    private ParamsBuilder() {
    }

    /**
     * 将MAP对象转换为List
     *
     * @param data
     *
     * @return
     */
    public static List<NameValuePair> objToParamList(Object data) {
        Map<String, Object> formMap = new HashMap<>();

        if (data instanceof Map) {
            formMap = (Map<String, Object>) data;
        } else {
            formMap = FastJsonUtil.objectToMap(data);
        }
        List<NameValuePair> list = new ArrayList<>();

        NameValuePair nameValuePair;
        for (Map.Entry<String, Object> entry : formMap.entrySet()) {
            String value = FastJsonUtil.toJSONString(entry.getValue());
            //System.out.println(value);
            logger.info("json string:\n{}", String.valueOf(value));
            nameValuePair = new BasicNameValuePair(entry.getKey(), value);
            list.add(nameValuePair);
        }

        return list;
    }


    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        Content content = new Content();
        content.setAppKey("123456");
        content.setcAccount(AccountConstant.NUMBER);
        map.put("a", 1);
        map.put("content", content);
        map.put("b", "hello");
        logger.info(FastJsonUtil.toPrettyJSONString(map));
        List<NameValuePair> list = null;
        list = objToParamList(map);
        System.out.println(FastJsonUtil.toPrettyJSONString(list));
    }
}
