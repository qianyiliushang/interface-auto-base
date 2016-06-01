package com.imo.test.utils;

import com.imo.test.bean.*;
import com.imo.test.constants.CommonBody;
import com.imo.test.constants.UrlConstants;
import com.jayway.restassured.path.json.JsonPath;
import com.zombie.http.HttpHelper;
import com.zombie.utils.base.RandomUtils;
import com.zombie.utils.encrypt.Base64Util;
import com.zombie.utils.encrypt.MD5Util;
import com.zombie.utils.json.FastJsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户,及企业注册通用工具类
 */

public class RegisterUtils {

    private static Logger logger = LogManager.getLogger(RegisterUtils.class);

    public static String userRegister(String number) {
        ReqDataRequest request = new ReqDataRequest();
        request.setQ(CommonBody.REGISTER);

        JsonPath jsonPath = JsonPath.from(applyCapchaCode(number));
        if (!jsonPath.get("retCode").equals("0")) {
            logger.error("apply capchacode error: {}", jsonPath.toString());
        }
        String code = CaptchaCodeUtil.getCaptchaCode(number);

        UserRegister register = new UserRegister();
        register.setCode(code);

        register.setName("autoTest" + RandomUtils.generateMixString(5));
        register.setType("2");
        register.setReqId(String.valueOf(System.currentTimeMillis()));
        register.setImo_channel_name("test");
        register.setPwd("passw0rd");
        register.setVersion("test");
        register.setNumber(number);

        request.setReqData(register);

        logger.info("request params:{}", request);

        return HttpHelper.doGetWithoutConfig(UrlConstants.IMOBASE, UrlConstants.SOCIAL, request);
    }

    public static String applyCapchaCode(String number) {
        ApplyCapchaCode applyCapchaCode = new ApplyCapchaCode();
        ReqDataRequest reqDataRequest = new ReqDataRequest();
        String time = String.valueOf(System.currentTimeMillis());
        String timeMd5 = MD5Util.encode(time);
        String numberMd5 = MD5Util.encode(number);
        String key = Base64Util.encode(numberMd5 + timeMd5);

        // Map<String,Object> params = new HashMap<>();
        //params.put("q",CommonBody.GETVALIDATECODE);
        // params.put("key",key);
        // params.put("time",time);
        // params.put("reqData",applyCapchaCode);
        reqDataRequest.setQ(CommonBody.GETVALIDATECODE);
        applyCapchaCode.setCounter("1");
        applyCapchaCode.setNumber(number);
        applyCapchaCode.setReqId(String.valueOf(System.currentTimeMillis()));
        applyCapchaCode.setType("1");
        applyCapchaCode.setKey(key);
        applyCapchaCode.setTime(time);
        reqDataRequest.setReqData(applyCapchaCode);
        // return HttpHelper.doPostWithoutConfig(UrlConstants.IMOBASE,UrlConstants.SOCIAL,params,reqDataRequest);
        return HttpHelper.doGetWithoutConfig(UrlConstants.IMOBASE, UrlConstants.SOCIAL, reqDataRequest);
    }


    public static String corpRegist(String number) {
        ReqDataRequest reqDataRequest = new ReqDataRequest();
        reqDataRequest.setQ(CommonBody.CORPREGISTER);
        CorpRegister corpRegister = new CorpRegister();
        JsonPath jsonPath = JsonPath.from(applyCapchaCode(number));
        if (!jsonPath.get("retCode").equals("0")) {
            logger.error("apply capchacode error: {}", jsonPath.toString());
        }
        String code = CaptchaCodeUtil.getCaptchaCode(number);
        corpRegister.setNumber(number);
        corpRegister.setPwd("password");
        corpRegister.setImo_channel_name("test");
        corpRegister.setName("autoTest" + RandomUtils.generateMixString(5));
        corpRegister.setVersion("test");
        corpRegister.setReqId(String.valueOf(System.currentTimeMillis()));
        corpRegister.setCode(code);
        corpRegister.setType("2");
        corpRegister.setCorpName("autoTest" + RandomUtils.generateMixString(5));
        reqDataRequest.setReqData(corpRegister);
        logger.info("corp register request params:{}", FastJsonUtil.toPrettyJSONString(reqDataRequest));
        return HttpHelper.doGetWithoutConfig(UrlConstants.IMOBASE, UrlConstants.SOCIAL, reqDataRequest);
    }


    public static Map<String, String> getCidAndToken(String number, String password) {
        Map<String, String> resultMap = new HashMap<>();
        Content content = new Content();
        content.setAppKey("testKey");
        //content.setcAccount("");
        content.setMobile(number);
        content.setPassword(password);
        content.setFlag("2");

        Login login = new Login();
        login.setContent(content);
        login.setChannel("autoTest");
        login.setProto("loginCenter get token req");
        login.setVer("1.2");
        login.setVersion("test");
        login.setNetEnv("1");
        login.setDevice("autoTest");
        login.setIdfa("test");
        login.setTranid(String.valueOf(System.currentTimeMillis()));
        JsontextRequest jsontextRequest = new JsontextRequest();
        jsontextRequest.setApp(CommonBody.SOCIALTOKEN);
        jsontextRequest.setJsontext(login);
        String response = HttpHelper.doGetWithoutConfig(UrlConstants.BASEIP, UrlConstants.OPENPLATFORM, jsontextRequest);
        System.out.println(response);
        JsonPath jsonPath = JsonPath.from(response);
        resultMap.put("cid", jsonPath.get("jsontext.cid"));
        resultMap.put("token", jsonPath.get("jsontext.token"));
        return resultMap;
    }

    public static void main(String[] args) {
        //System.out.println(applyCapchaCode(RandomUtils.getTelNum()));
        //System.out.println(userRegister(RandomUtils.getTelNum()).toString());
        System.out.println(corpRegist(RandomUtils.getTelNum()).toString());
        // String number = RandomUtils.getTelNum();
        //String number = "13102823263";
        // String password = "password";
        // corpRegist(number);
        // getCidAndToken(number, password);

    }
}
