package com.imo.test.bean;

import com.zombie.utils.encrypt.MD5Util;

/**
 * 企业注册
 */

public class CorpRegister {

    /**
     * 参数名：reqData
     * 参数值：{"number":"xxxxxxx","name":"李小明","pwd":"xxxxxxxxxxxxxxx","code":"1234","type":"2","reqId":"1234568"}
     * number - 手机号
     * name - 用户名称
     * corpName - 公司名称
     * cid - 申请公司的cid（如果为空则表示自己创建组织，如果有值则表示申请加入组织）
     * pwd - 用户密码（客户端需要加密码，加密方法同pc MD5 一次）
     * code - 验证码
     * imo_channel_name - 渠道
     * version - 版本
     * type - 注册来源（"0"-官网注册 "1"-移动官网注册 "2"-手机注册 "3"-管理后台 "4"-开放平台） -- 不传该参数默认为 "2"
     * case - 1 方案A ，2 方案B
     * reqId - 请求的事务id（guid）
     */

    private String number;
    private String name;
    private String corpName;
    private String cid;
    private String pwd;
    private String code;
    private String imo_channel_name;
    private String version;
    private String type;
    //  private String case;
    private String reqId;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = MD5Util.encode(pwd);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImo_channel_name() {
        return imo_channel_name;
    }

    public void setImo_channel_name(String imo_channel_name) {
        this.imo_channel_name = imo_channel_name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
}
