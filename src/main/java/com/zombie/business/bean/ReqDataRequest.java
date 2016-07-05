package com.zombie.business.bean;

import com.zombie.base.BaseRequest;

/**
 *
 */

public class ReqDataRequest extends BaseRequest{
    private String q;
    private Object reqData;

    /**
     * 请求对应的API地址,务必在子类中重写此方法
     *
     * @return 请求对应的API地址
     */
    @Override
    public String url() {
        return UrlConstants.IMOBASE+UrlConstants.SOCIAL;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public Object getReqData() {
        return reqData;
    }

    public void setReqData(Object reqData) {
        this.reqData = reqData;
    }
}
