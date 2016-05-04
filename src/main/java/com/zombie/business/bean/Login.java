package com.zombie.business.bean;

/**
 * 登录中心登录且获取用户的功能开关信息
 */

public class Login {
    /**
     * tranid - 事务id，由客户端计算给出
     * ver - 接口版本号，默认用1.1
     * device - 设备标识，0-pc、1-android、2-iOS、3-Mac OS
     * - netEnv - 网络环境
     * UNKNOWN = '0';
     * WIFI = '1';
     * 2G = '2';
     * 3G = '3';
     * 4G = '4';
     * content - 具体内容：
     * appKey - 移动客户端统一的appKey
     * cAccount - 组织账号，如果非手机登录需要这个参数
     * uAccount - 用户账号，如果非手机登录需要这个参数
     * mobile - 手机号
     * password - 密码（客户端需要加密）
     * sUid - 第三方登录唯一标识
     * sToken - 第三方登录得到的token
     * flag - 登录标识,0-数字帐号登录;1-域名帐号登录;2-绑定的手机号登录;3, weixin; 4, qq;
     * idfa - iOS IDFA 标识
     */

    private String proto;
    private String tranid;
    private String ver;
    private String device;
    private String idfa;
    private String channel;
    private String version;
    private String netEnv;

    private Content content;

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    public String getTranid() {
        return tranid;
    }

    public void setTranid(String tranid) {
        this.tranid = tranid;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNetEnv() {
        return netEnv;
    }

    public void setNetEnv(String netEnv) {
        this.netEnv = netEnv;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
