package com.zombie.business.bean;

import com.zombie.utils.encrypt.MD5Util;

/**
 * 用户注册
 */

public class UserRegister {
    /**
     * 参数名：reqData
     * 参数值：{"number":"xxxxxxx","name":"李小明","pwd":"xxxxxxxxxxxxxxx","code":"1234","type":"2","reqId":"1234568"}
     * number - 手机号
     * name - 用户名称
     * pwd - 用户密码（客户端需要加密码，加密方法同pc）
     * code - 验证码
     * imo_channel_name - 渠道
     * version - 版本
     * type - 注册来源（"0"-官网注册 "1"-移动官网注册 "2"-手机注册 "3"-管理后台 "4"-开放平台） -- 不传该参数默认为 "2"
     * inviteCode - 邀请码 （非必传）
     * --- android设备标示参数 --- (非必传)
     * IMSI - android设备标示
     * IMEI - android设备标示
     * MAC  - android设备标示
     * --- end ----
     * <p>
     * bindCode - 红包推荐码（非必传）
     * <p>
     * reqId - 请求的事务id（guid）
     */

    private String number;
    private String name;
    private String pwd;
    private String code;
    private String reqId;
    private String imo_channel_name;
    private String type;
    private String inviteCode;
    private String IMSI;
    private String IMEI;
    private String MAC;
    private String bindCode;
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getIMSI() {
        return IMSI;
    }

    public void setIMSI(String IMSI) {
        this.IMSI = IMSI;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getBindCode() {
        return bindCode;
    }

    public void setBindCode(String bindCode) {
        this.bindCode = bindCode;
    }

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

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getImo_channel_name() {
        return imo_channel_name;
    }

    public void setImo_channel_name(String imo_channel_name) {
        this.imo_channel_name = imo_channel_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
