package com.zombie.business;


import com.jayway.restassured.path.json.JsonPath;
import com.zombie.business.bean.*;
import com.zombie.http.HttpHelper;
import com.zombie.utils.base.RandomUtils;
import com.zombie.utils.encrypt.Base64Util;
import com.zombie.utils.encrypt.MD5Util;
import com.zombie.utils.json.FastJsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户,及企业注册通用工具类
 */

public class RegisterUtils {

    private static Logger logger = LogManager.getLogger(RegisterUtils.class);

    public static String userRegister(String number) {
        return userRegister(number, "password");
    }

    public static String userRegister(String phoneNumber, String password) {
        ReqDataRequest request = new ReqDataRequest();
        request.setQ(CommonBody.REGISTER);

        JsonPath jsonPath = JsonPath.from(applyCapchaCode(phoneNumber));
        if (!jsonPath.get("retCode").equals("0")) {
            logger.error("apply capchacode error: {}", jsonPath.toString());
        }
        String code = CaptchaCodeUtil.getCaptchaCode(phoneNumber);

        UserRegister register = new UserRegister();
        register.setCode(code);

        register.setName("autoTest" + RandomUtils.generateMixString(5));
        register.setType("2");
        register.setReqId(String.valueOf(System.currentTimeMillis()));
        register.setImo_channel_name("test");
        register.setPwd(password);
        register.setVersion("test");
        register.setNumber(phoneNumber);

        request.setReqData(register);

        logger.info("request params:{}", request);

        return HttpHelper.doGetWithoutConfig(UrlConstants.IMOBASE, UrlConstants.SOCIAL, request);
    }

    /**
     * 默认提供注册类型
     *
     * @param number 手机号
     *
     * @return
     */
    public static String applyCapchaCode(String number) {
        ApplyCapchaCode applyCapchaCode = new ApplyCapchaCode();
        ReqDataRequest reqDataRequest = new ReqDataRequest();

        String time = String.valueOf(System.currentTimeMillis());
        String timeMd5 = MD5Util.encode(time);
        String numberMd5 = MD5Util.encode(number);
        String key = Base64Util.encode(numberMd5 + timeMd5);

        reqDataRequest.setQ(CommonBody.GETVALIDATECODE);
        applyCapchaCode.setCounter("1");
        applyCapchaCode.setNumber(number);
        applyCapchaCode.setReqId(String.valueOf(System.currentTimeMillis()));
        applyCapchaCode.setType("1");
        applyCapchaCode.setTime(time);
        applyCapchaCode.setKey(key);
        reqDataRequest.setReqData(applyCapchaCode);
        return HttpHelper.doGetWithoutConfig(UrlConstants.IMOBASE, UrlConstants.SOCIAL, reqDataRequest);
    }

    /**
     * @param number 手机号
     * @param type   1-注册，2-重置密码，3-绑定手机号（非真正的绑定，只是更新手机信息），4-登录
     *
     * @return
     */
    public static String applyCpchaCode(String number, String type) {
        ApplyCapchaCode applyCapchaCode = new ApplyCapchaCode();
        ReqDataRequest reqDataRequest = new ReqDataRequest();

        String time = String.valueOf(System.currentTimeMillis());
        String timeMd5 = MD5Util.encode(time);
        String numberMd5 = MD5Util.encode(number);
        String key = Base64Util.encode(numberMd5 + timeMd5);
        reqDataRequest.setQ(CommonBody.GETVALIDATECODE);
        applyCapchaCode.setCounter("1");
        applyCapchaCode.setNumber(number);
        applyCapchaCode.setReqId(String.valueOf(System.currentTimeMillis()));
        applyCapchaCode.setType(type);
        applyCapchaCode.setTime(time);
        applyCapchaCode.setKey(key);
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
        //  logger.info("response of login:\n{}", jsonPath.prettyPrint());
        List<Map<String, String>> cidList = jsonPath.getList("jsontext.cid");
        if (cidList.size() != 0) {
            resultMap.put("cid", cidList.get(0).get("cid"));
        }
        resultMap.put("token", jsonPath.get("jsontext.token"));
        resultMap.put("uid", jsonPath.get("jsontext.uid"));
        // resultMap.put("uAccout", jsonPath.get("uAcount"));
        resultMap.put("cAccount", jsonPath.getString("jsontext.cAccount"));
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

    public static Map<String, Object> userRegistAndGetUid(String phoneNumber, String password) {
        userRegister(phoneNumber, password);
        return getCidAndToken(phoneNumber, password);
    }

    public static void main(String[] args) {
        //System.out.println(applyCapchaCode(RandomUtils.getTelNum()));
        //System.out.println(userRegister("17717064445"));
        //System.out.println(getCid("17717064445", "password"));

        List<Map<String, Object>> resultList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            String number = RandomUtils.getSpecPhoneNumber();
            String password = RandomUtils.generateMixString(6);
            System.out.println("regist result" + corpRegist(number, password).toString());
            corpRegist(number, password);
            Map<String, Object> result = getCidAndToken(number, password);
            result.remove("token");
            result.put("phoneNumber", number);
            result.put("password", password);

            resultList.add(result);
        }
        String rootPath = System.getProperty("user.dir");
        File file = new File(rootPath, "testdata.txt");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(FastJsonUtil.toPrettyJSONString(resultList).getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // String number = RandomUtils.getTelNum();
        // String number = "13204754205";
        //String password = "password";
        // corpRegist(number);
        // getCidAndToken(number, password);

        // System.out.println(getDefaultCid());
        // System.out.println(getDefaultToken());

        //System.out.println(FastJsonUtil.toPrettyJSONString(getCidAndToken(AccountConstant.NUMBER, AccountConstant.PASSWORD)));
        // Map<String, Object> result = RegisterUtils.getCidAndToken("15706167938", "passw0rd");
        //Map<String, Object> userMap;
        //userMap = result;
        // System.out.println(FastJsonUtil.toPrettyJSONString(userMap));

    }
}
