package com.zombie.business;

import com.jayway.restassured.path.json.JsonPath;
import com.zombie.http.HttpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * API通用方法
 */

public class ApiUtils {
    private static Logger logger = LoggerFactory.getLogger(ApiUtils.class);

    public static String getValueFromRedis(String key) {
        String baseUrl = "http://debug.imoffice.cn/index.php";
        Map<String, String> params = new HashMap<>();
        params.put("uri", "Api/redis");
        params.put("action", "get");
        params.put("key", key);
        JsonPath jsonPath = JsonPath.from(HttpHelper.doGetWithoutConfig(baseUrl, null, params));
        // logger.info("response from redis is:\n{}", jsonPath.prettyPrint());
        return jsonPath.getString("val");
    }

    public static String getCodeFromRedis(String number, int type) {
        String social = "social_";
        String reg = "_reg_validate";
        String reset = "_reset_validate";
        String bind = "verify_code_";
        String login = "_login_validate";
        String key = null;
        switch (type) {
            case 1:
                key = social + number + reg;
                break;
            case 2:
                key = social + number + reset;
                break;
            case 3:
                key = social+bind + number;
                break;
            case 4:
                key = social + number + login;
            default:
                break;
        }

        return getValueFromRedis(key);
    }

    public static void main(String[] args) {
        System.out.println(getValueFromRedis("social_verify_code_18502292805"));
        System.out.println(getCodeFromRedis("18502292805",3));
    }
}
