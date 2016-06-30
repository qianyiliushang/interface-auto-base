package com.zombie.base;

/**
 * HTTP基础请求
 */

public abstract class BaseRequest {

    /**
     * 请求对应的API地址,务必在子类中重写此方法
     *
     * @return 请求对应的API地址
     */
    public abstract String url();

}
