package com.zombie.test;

import com.zombie.bean.GetBrandRequest;
import com.zombie.bean.SimpleEnum;
import com.zombie.utils.base.CommonUtils;
import com.zombie.utils.base.ParamsBuilder;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;

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


}
