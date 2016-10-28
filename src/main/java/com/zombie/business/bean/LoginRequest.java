package com.zombie.business.bean;

import com.zombie.base.BaseRequest;
import com.zombie.utils.config.ConfigUtil;

/**
 *
 */

public class LoginRequest extends BaseRequest {

    private Login jsontext;

    public Login getJsontext() {
        return jsontext;
    }

    public void setJsontext(Login jsontext) {
        this.jsontext = jsontext;
    }

    @Override
    public String url() {
        String url = null;
        String env = ConfigUtil.get("env");
        int environment = Integer.parseInt(env);
        switch (environment) {
            case 0:
                url = "http://222.73.33.230/OpenPlatform/?app=cSocialToken";
                break;
            case 1:
                url = "http://open.imoffice.com:8000/?app=cSocialToken";
                break;
            default:
                url = "http://222.73.33.230/OpenPlatform/?app=cSocialToken";
        }
        return url;
    }
}
