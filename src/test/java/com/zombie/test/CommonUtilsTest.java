package com.zombie.test;

import com.zombie.bean.SimpleEnum;
import com.zombie.utils.base.CommonUtils;
import com.zombie.utils.json.FastJsonUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 *
 *
 */

@Test
public class CommonUtilsTest {


    public void arraysToString() {
        String[] strArray = {"aaa", "bbbb", "ccc"};
        String result = CommonUtils.assembleArray(strArray, true);
        assertTrue(result.contains("|"));
    }

    public void formatTest() {
        List<NameValuePair> pairList = new ArrayList<>();
        pairList.add(new BasicNameValuePair("name", "zombie"));
        pairList.add(new BasicNameValuePair("nick", "qianyiliushang"));
        String params = CommonUtils.format(pairList);
        System.out.println(params);
        assertTrue(params.contains("="));
    }

    public void enumTest() {
        SimpleEnum[] simpleEna = {SimpleEnum.MON, SimpleEnum.TUE, SimpleEnum.WEN};
        Object[][] objects = CommonUtils.enumToArray(simpleEna);
        for (int i = 0; i < objects.length; i++) {
            for (int j = 0; j < objects[i].length; j++) {
                System.out.println(objects[i][j]);
            }
        }
    }

    public void alphabetTest() {
        String[] ss = CommonUtils.getAllAlphabet();
        for (int i = 0; i < ss.length; i++) {
            System.out.println(ss[i]);
        }
    }

    public void listToString() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        StringBuilder sb = new StringBuilder();
        for (Integer i : list) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(i);
        }
        System.out.println(sb.toString());
    }

    public List<Integer> stringToList(String str) {
        String[] ss = str.split(",");
        List<Integer> list = new ArrayList<>();
        for (String s : ss) {
            list.add(Integer.parseInt(s));
        }
        return list;
    }

    public void stringToListTest() {
        String ss = "1,2,3";
        System.out.println(FastJsonUtil.toJSONString(stringToList(ss)));
    }

    public void urlDecode() throws UnsupportedEncodingException {
        String url1 = "http://imoapp.imoffice.cn/social?reqData=%7B%22number%22%3A%2218502292805%22%2C%22counter%22%3A%221%22%2C%22time%22%3A%221467767690644%22%2C%22type%22%3A%222%22%2C%22key%22%3A%22NzM1MjBmMmNmY2E0YTk1OWIwYzM2ZjZiMTM1NmFlMDcxZDE5NWYyMGFjODM2MTIyMDQwMTM4ZTQ4N2Q5MzdmOQ%3D%3D%22%2C%22reqId%22%3A%221467767695503%22%7D&q=get%2FverifyCode";
        String url2 = "http://imoapp.imoffice.cn/social?q=get%2FverifyCode&reqData=%7Bnumber%3D15803957801%2C+type%3D1%2C+reqId%3D1467767645594%2C+counter%3D1%2C+key%3DNDgxNGNlMWE5NDkzYmRiNmY5N2ZlNThkMzMzYjMzY2MxMTUwMzY5NGY0Y2QwZmM3ZTg3MTgxNTc2ZjM2MTMyZg%3D%3D%2C+time%3D1467767645589%7D";
        String url3 = "q=get%2FverifyCode&reqData=%7Bnumber%3D18502292805%2C+type%3D1%2C+reqId%3D1467769546076%2C+counter%3D1%2C+key%3DNzM1MjBmMmNmY2E0YTk1OWIwYzM2ZjZiMTM1NmFlMDcxNmQ0NmNhZGZkZTJkNDIyOTc0OTVlMzU1OTgwODE0Zg%3D%3D%2C+time%3D1467769546034%7D\n";
        System.out.println(URLDecoder.decode(url1, "UTF-8"));
        System.out.println(URLDecoder.decode(url2, "UTF-8"));
        System.out.println(URLDecoder.decode(url3, "UTF-8"));
    }

}
