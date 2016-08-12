package com.zombie.business;

import com.alibaba.fastjson.JSONObject;
import com.jayway.restassured.path.json.JsonPath;
import com.zombie.business.bean.*;
import com.zombie.httpclient.HCHelper;
import com.zombie.utils.encrypt.Base64Util;
import com.zombie.utils.encrypt.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */

public class CommonApiUtils {
    static Logger logger = LoggerFactory.getLogger(CommonApiUtils.class);

    public static boolean applyCapchaCode(String phoneNumber) {
        ApplyCapchaCode applyCapchaCode = new ApplyCapchaCode();
        Long time = System.currentTimeMillis();
        String timeMd5 = MD5Util.encode(time.toString());
        String numberMd5 = MD5Util.encode(phoneNumber);
        String key = Base64Util.encode(numberMd5 + timeMd5);
        applyCapchaCode.setCounter("1");
        applyCapchaCode.setNumber(phoneNumber);
        applyCapchaCode.setReqId(time.toString());
        applyCapchaCode.setType("1");
        applyCapchaCode.setKey(key);
        applyCapchaCode.setTime(String.valueOf(time));

        ApplyCapchaCodeRequest request = new ApplyCapchaCodeRequest();
        request.setReqData(applyCapchaCode);
        JSONObject jsonObject = HCHelper.postForm(request);
        int errCode = jsonObject.getIntValue("errCode");
        if (errCode == 0) {
            return true;
        } else {
            return false;
        }

    }


    public static JSONObject userLogin(String phoneNumber, String password) {
        LoginRequest loginRequest = new LoginRequest();
        Login login = new Login();
        Content content = new Content();
        content.setAppKey("testKey");
        content.setMobile(phoneNumber);
        content.setPassword(password);
        content.setFlag("2");

        login.setContent(content);
        login.setChannel("autoTest");
        login.setProto("loginCenter get token req");
        login.setVer("1.2");
        login.setVersion("test");
        login.setNetEnv("1");
        login.setDevice("autoTest");
        login.setIdfa("test");
        login.setTranid(String.valueOf(System.currentTimeMillis()));
        loginRequest.setJsontext(login);

        JSONObject response = HCHelper.postForm(loginRequest);
        if (response.getIntValue("retCode") != 0) {
            logger.info("登录失败");
            System.exit(-1);
        }

        return response;
    }



}
