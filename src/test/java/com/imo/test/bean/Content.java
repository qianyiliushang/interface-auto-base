package com.imo.test.bean;

import com.zombie.utils.encrypt.MD5Util;

/**
 *
 */

public class Content {
    /**
     * content - 具体内容：
     * appKey - 移动客户端统一的appKey
     * cAccount - 组织账号，如果非手机登录需要这个参数
     * uAccount - 用户账号，如果非手机登录需要这个参数
     * mobile - 手机号
     * password - 密码（客户端需要加密）
     * sUid - 第三方登录唯一标识
     * sToken - 第三方登录得到的token
     * flag - 登录标识,0-数字帐号登录;1-域名帐号登录;2-绑定的手机号登录;3, weixin; 4, qq;
     */

    private String appKey;
    private String cAccount;
    private String uAccount;
    private String mobile;
    private String password;
    private String sUid;
    private String sToken;
    private String flag;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getcAccount() {
        return cAccount;
    }

    public void setcAccount(String cAccount) {
        this.cAccount = cAccount;
    }

    public String getuAccount() {
        return uAccount;
    }

    public void setuAccount(String uAccount) {
        this.uAccount = uAccount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = MD5Util.encode(password);
    }

    public String getsUid() {
        return sUid;
    }

    public void setsUid(String sUid) {
        this.sUid = sUid;
    }

    public String getsToken() {
        return sToken;
    }

    public void setsToken(String sToken) {
        this.sToken = sToken;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
