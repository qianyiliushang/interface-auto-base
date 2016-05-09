package com.zombie.business;


import com.jayway.restassured.path.json.JsonPath;
import com.zombie.business.bean.*;
import com.zombie.http.HttpHelper;
import com.zombie.utils.base.RandomUtils;
import com.zombie.utils.json.FastJsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
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
        reqDataRequest.setQ(CommonBody.GETVALIDATECODE);
        applyCapchaCode.setCounter("1");
        applyCapchaCode.setNumber(number);
        applyCapchaCode.setReqId(String.valueOf(System.currentTimeMillis()));
        applyCapchaCode.setType("1");
        reqDataRequest.setReqData(applyCapchaCode);
        return HttpHelper.doGetWithoutConfig(UrlConstants.IMOBASE, UrlConstants.SOCIAL, reqDataRequest);
    }

    public static String corpRegist(String number, String password) {
        ReqDataRequest reqDataRequest = new ReqDataRequest();
        reqDataRequest.setQ(CommonBody.CORPREGISTER);
        CorpRegister corpRegister = new CorpRegister();
        JsonPath jsonPath = JsonPath.from(applyCapchaCode(number));
        if (!jsonPath.get("retCode").equals("0")) {
            logger.error("apply capchacode error: {}", jsonPath.toString());
        }
        String code = CaptchaCodeUtil.getCaptchaCode(number);
        corpRegister.setNumber(number);
        corpRegister.setPwd(password);
        corpRegister.setImo_channel_name("test");
        corpRegister.setName("autoTest" + RandomUtils.generateMixString(5));
        corpRegister.setVersion("test");
        corpRegister.setReqId(String.valueOf(System.currentTimeMillis()));
        corpRegister.setCode(code);
        corpRegister.setType("2");
        //corpRegister.setCid(RandomUtils.generateNumberString(6));
        corpRegister.setCorpName("autoTest" + RandomUtils.generateMixString(5));
        reqDataRequest.setReqData(corpRegister);
        return HttpHelper.doGetWithoutConfig(UrlConstants.IMOBASE, UrlConstants.SOCIAL, reqDataRequest);
    }


    public static Map<String, Object> getCidAndToken(String number, String password) {
        Map<String, Object> resultMap = new HashMap<>();
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
        login.setTranid(String.valueOf(System.currentTimeMillis()));
        JsontextRequest jsontextRequest = new JsontextRequest();
        jsontextRequest.setApp(CommonBody.SOCIALTOKEN);
        jsontextRequest.setJsontext(login);

        String response = HttpHelper.doGetWithoutConfig(UrlConstants.BASEIP, UrlConstants.OPENPLATFORM, jsontextRequest);
        JsonPath jsonPath = JsonPath.from(response);
        List<Map<String, Object>> cidList = jsonPath.getList("jsontext.cid");
        if (cidList.size() != 0) {
            resultMap.put("cid", cidList.get(0).get("cid"));
        }
        resultMap.put("token", jsonPath.get("jsontext.token"));
        resultMap.put("uid", jsonPath.get("jsontext.uid"));
        return resultMap;
    }

    public static Map<String, Object> getDefaultCidAndToken() {
        return getCidAndToken(AccountConstant.NUMBER, AccountConstant.PASSWORD);
    }

    public static String getToken(String number, String password) {
        Map<String, Object> resultMap = getCidAndToken(number, password);
        return resultMap.get("token").toString();
    }

    public static String getCid(String number, String password) {
        Map<String, Object> resultMap = getCidAndToken(number, password);
        return resultMap.get("cid").toString();
    }

    public static String getDefaultToken() {
        return getCidAndToken(AccountConstant.NUMBER, AccountConstant.PASSWORD).get("token").toString();
    }

    public static String getDefaultCid() {
        // List<String> cidList = getCidAndToken(AccountConstant.NUMBER, AccountConstant.PASSWORD).get("cid");
        // return cidList.get(0);
        return getCidAndToken(AccountConstant.NUMBER, AccountConstant.PASSWORD).get("cid").toString();

    }

    public static String regCorpAndGetCid(String number, String password) {
        JsonPath registResponse = JsonPath.from(corpRegist(number, password));
        if (!registResponse.get("retCode").equals("0")) {
            logger.error("corp register error");
            return null;
        }
        return getCid(number, password);

    }

    public static void main(String[] args) {
        //System.out.println(applyCapchaCode(RandomUtils.getTelNum()));
       // System.out.println(userRegister(RandomUtils.getTelNum()).toString());
        //System.out.println(corpRegist(RandomUtils.getTelNum(), "passw0rd").toString());
        // String number = RandomUtils.getTelNum();
        // String number = "13204754205";
        //String password = "password";
        // corpRegist(number);
        // getCidAndToken(number, password);

        // System.out.println(getDefaultCid());
        // System.out.println(getDefaultToken());

        //System.out.println(FastJsonUtil.toPrettyJSONString(getCidAndToken(AccountConstant.NUMBER, AccountConstant.PASSWORD)));
        Map<String, Object> result = RegisterUtils.getCidAndToken("15706167938", "passw0rd");
        Map<String, Object> userMap;
        userMap = result;
        System.out.println(FastJsonUtil.toPrettyJSONString(userMap));

    }
}
