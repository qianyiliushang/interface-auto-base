package com.zombie.utils.base;

import com.zombie.utils.config.ConfigUtil;
import com.zombie.utils.json.GsonUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 对于get请求或者需要在url后添加参数的post请求,构建请求URL
 */

public class ParamsBuilder {
    private static Logger logger = LoggerFactory.getLogger(ParamsBuilder.class);

    /**
     * 构建get请求url,需要事先在config.properties中配置好baseURL
     *
     * @param uri       接口地址
     * @param paramsMap 参数map
     *
     * @return url
     */
    public static URL paramsBuiler(String uri, Map<Object, Object> paramsMap) {
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        String params = null;
        String destURL = ConfigUtil.get("baseURL");
        URL url = null;
        // final StringBuilder result = new StringBuilder();
        if (ValidateHelper.isNotEmptyMap(paramsMap)) {
            Iterator<Map.Entry<Object, Object>> iterator = paramsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, Object> entry = iterator.next();
                nameValuePairList.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
            }
            params = CommonUtils.format(nameValuePairList);

            destURL = destURL + uri + "?" + params;

        }
        try {
            url = new URL(destURL);
        } catch (MalformedURLException e) {
            logger.error("构建URL失败");
        }
        return url;
    }


    public static String getFormData(Object object) {
        Map<String, Object> formMap = GsonUtils.jsonObjectToMap(object);
        final StringBuilder result = new StringBuilder();
        if (ValidateHelper.isNotEmptyMap(formMap)) {
            Iterator<Map.Entry<String, Object>> iterator = formMap.entrySet().iterator();
            while (iterator.hasNext()) {
                if (result.length() > 0) {
                    result.append("&");
                }
                Map.Entry entry = iterator.next();
                result.append(entry.getKey()).append("=").append(entry.getValue());
            }

        }


        return result.toString();
    }
}
