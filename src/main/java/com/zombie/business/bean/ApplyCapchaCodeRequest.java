package com.zombie.business.bean;

import com.zombie.base.BaseRequest;
import com.zombie.utils.config.ConfigUtil;

/**
 *
 */

public class ApplyCapchaCodeRequest extends BaseRequest {
    private ApplyCapchaCode reqData;

    public ApplyCapchaCode getReqData() {
        return reqData;
    }

    public void setReqData(ApplyCapchaCode reqData) {
        this.reqData = reqData;
    }

    /**
     * 请求对应的API地址,务必在子类中重写此方法
     *
     * @return 请求对应的API地址
     */
    @Override
    public String url() {
        String url = null;
        String env = ConfigUtil.get("env");
        int environment = Integer.parseInt(env);
        switch (environment) {
            case 0:
                url = "http://imoapp.imoffice.cn/social/?q=get/verifyCode";
                break;
            case 1:
                url = "http://imoapp.imoffice.com:8000/social/?q=get/verifyCode";
                break;
            default:
                url = "http://imoapp.imoffice.cn/social/?q=get/verifyCode";
        }
        return url;
    }
}
