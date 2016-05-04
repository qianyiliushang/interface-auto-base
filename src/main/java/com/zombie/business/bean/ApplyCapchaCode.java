package com.zombie.business.bean;

/**
 * 获取验证码请求body
 */

public class ApplyCapchaCode {
    //手机号（经过合法校验过的）
    private String number;

    //验证码类型 1-注册，2-重置密码，3-绑定手机号（非真正的绑定，只是更新手机信息）
    private String type;

    //请求的事务id（guid）
    private String reqId;

    //获取验证码计数器，后端根据这个数字的奇、偶来选择不同的短信通道
    private String counter;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }
}
