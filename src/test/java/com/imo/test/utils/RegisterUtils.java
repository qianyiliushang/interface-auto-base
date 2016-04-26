package com.imo.test.utils;

import com.imo.test.bean.ApplyCapchaCode;
import com.imo.test.bean.BaseRequest;
import com.imo.test.bean.UserRegister;
import com.imo.test.constants.UrlConstants;
import com.jayway.restassured.path.json.JsonPath;
import com.zombie.http.HttpHelper;
import com.zombie.utils.base.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 用户,及企业注册通用工具类
 */

public class RegisterUtils {

    private static Logger logger = LogManager.getLogger(RegisterUtils.class);

    public static String userRegister(String number) {
        BaseRequest request = new BaseRequest();
        request.setQ(UrlConstants.REGISTER);

        JsonPath jsonPath = JsonPath.from(applyCapchaCode(number));
        if (!jsonPath.get("retCode").equals("0")) {
            //logger.error("apply capchacode error,{}", jsonPath.get("errMsg"));
            logger.error("apply capchacode error: {}", jsonPath.toString());

        }

        UserRegister register = new UserRegister();
        register.setName("autoTest" + RandomUtils.generateMixString(5));


        String code = CaptchaCodeUtil.getCaptchaCode(number);
        register.setCode(code);

        return null;
    }

    public static String applyCapchaCode(String number) {
        ApplyCapchaCode applyCapchaCode = new ApplyCapchaCode();
        BaseRequest baseRequest = new BaseRequest();
        baseRequest.setQ(UrlConstants.GETVALIDATECODE);

        applyCapchaCode.setCounter("1");
        applyCapchaCode.setNumber(number);
        applyCapchaCode.setReqId(String.valueOf(System.currentTimeMillis()));
        applyCapchaCode.setType("1");
        baseRequest.setReqData(applyCapchaCode);

        return HttpHelper.doGetWithoutConfig(UrlConstants.IMOBASE, null, baseRequest);
    }

    public static void main(String[] args) {
        System.out.println(applyCapchaCode(RandomUtils.getTelNum()).toString());
    }
}
