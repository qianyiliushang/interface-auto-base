package com.zombie.http;

import org.omg.CORBA.DataOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Created by chenpengpeng on 16/4/15.
 * <p>
 * 使用HttpUrlConnection发送各种请求
 */
public class HttpHelper {
    private static Logger logger = LoggerFactory.getLogger(HttpHelper.class);


    public static String doJsonPost(Object request, String url) {
        DataOutputStream outputStream = null;
        InputStream inputStream = null;
        String jsonRequest;
        return null;
    }
}
