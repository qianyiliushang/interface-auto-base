package com.zombie.business;

import com.zombie.utils.base.RegexUtils;
import com.zombie.utils.base.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 验证码相关工具类
 * Created by zombie on 16/1/12.
 */
public class CaptchaCodeUtil {

    /**
     * 根据手机号获取验证码,注意,请一定在点击发送验证码之后再调用此方法
     * 当传入的手机号与实际要获取的手机号不符,验证码已超时时,返回Null
     *
     * @param phoneNumber 需要获取验证码的手机号
     *
     * @return 验证码
     */
    public static String getCaptchaCode(String phoneNumber) {
        //外测环境
        String baseUrl = "http://imoapp.imoffice.cn/social/code.php?number=";

        //正式环境
        // String baseUrl = "http://imoapp.imoffice.com:8000/social/code.php?token=6202393DCC6B4301BE21A573BBCE6848&number=";
        String finalUrl = baseUrl + phoneNumber;
        String captchaCode = null;
        HttpURLConnection httpURLConnection;
        try {
            URL url = new URL(finalUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            InputStream inputStream;
            inputStream = httpURLConnection.getInputStream();
            byte[] readStream = StringUtils.readStream(inputStream);
            String responseStr = new String(readStream, "UTF-8");
            captchaCode = RegexUtils.getNumberInString(responseStr);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return captchaCode;
    }

    public static void main(String[] args) {
        String code = getCaptchaCode("13024178285");
        System.out.println(code);
    }

}
