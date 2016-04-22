package com.zombie.test;

import com.zombie.bean.GetBrandRequest;
import com.zombie.utils.base.ParamsBuilder;
import com.zombie.utils.json.GsonUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 */

@Test
public class GsonUtilTest {
    String request = "{" +
            "    \"brand\":[\"apple\",\"google\"]," +
            "    \"start\":0," +
            "    \"end\":9" +
            "}";
    GetBrandRequest getBrandRequest;

    @BeforeClass
    public void setUp() {
        getBrandRequest = new GetBrandRequest();
        List<String> brandList = new ArrayList<>();
        brandList.add("apple");
        brandList.add("google");
        getBrandRequest.setBrand(brandList);
        getBrandRequest.setStart(0);
        getBrandRequest.setEnd(10);
    }

    public void parseString() {

        System.out.println(GsonUtils.parseJson(request));
    }

    public void prettyJsonString() {
        System.out.println(GsonUtils.formatJsonString(request));
    }

    public void parseBean() {
        System.out.println(GsonUtils.parseJson(getBrandRequest));
    }

    public void jsonStringToMapTest() {
        Map<String, Object> resultMap = GsonUtils.jsonObjectToMap(request);
        Iterator<Map.Entry<String, Object>> iterator = resultMap.entrySet().iterator();
        final StringBuilder result = new StringBuilder();
    }

    public void beanToMapTest() {
        Map<String, Object> resultMap = GsonUtils.jsonObjectToMap(getBrandRequest);
        Iterator<Map.Entry<String, Object>> iterator = resultMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            System.out.println(entry.getKey() + ":" + entry.getValue() + "\n");
        }
    }

    public void formData() {
        System.out.println(ParamsBuilder.getFormData(getBrandRequest));
    }

    public void forDataFromString() {
        System.out.println(ParamsBuilder.getFormData(request));
    }
}
