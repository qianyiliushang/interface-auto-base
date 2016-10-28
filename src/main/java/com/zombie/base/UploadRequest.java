package com.zombie.base;

/**
 *
 */

public class UploadRequest extends BaseRequest{
    /**
     * 请求对应的API地址,务必在子类中重写此方法
     *
     * @return 请求对应的API地址
     */
    @Override
    public String url() {
        return "http://139.196.189.53:8080/imgsvc/api/pb/v1/img/upload/op";
    }
}
