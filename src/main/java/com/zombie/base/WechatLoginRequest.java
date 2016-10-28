package com.zombie.base;

/**
 *
 */

public class WechatLoginRequest extends BaseRequest {
    /**
     * 请求对应的API地址,务必在子类中重写此方法
     *
     * @return 请求对应的API地址
     */
    @Override
    public String url() {
        return "http://139.196.189.53:8080/web/api/pb/v1/register/wechat";
    }

    private String openid;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
