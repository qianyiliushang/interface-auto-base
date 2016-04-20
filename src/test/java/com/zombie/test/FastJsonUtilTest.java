package com.zombie.test;

import com.zombie.utils.json.FastJsonUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenpengpeng on 16/4/15.
 */
public class FastJsonUtilTest {
    FassJson fassJson = new FassJson();
    String jsonString;

    @BeforeClass
    public void setUp() {
        fassJson.setAge(1);
        fassJson.setTitle("test");
        List<String> otherList = new ArrayList<>();
        otherList.add("aaa");
        otherList.add("bbb");
        fassJson.setOther(otherList);
        jsonString = FastJsonUtil.objectToJsonString(fassJson);
    }

    @Test
    public void toStringTest() {
        System.out.println(jsonString);
    }

    @Test
    public void toPrettyString() {
        System.out.println(FastJsonUtil.toPrettyJSONString(fassJson));
    }

    @Test
    public void stringToVo() {
        FassJson json = FastJsonUtil.parseObject(jsonString, FassJson.class);
        System.out.println(json.getAge());
        System.out.println(json.getTitle());
        System.out.println(json.getOther().toString());
    }


}
