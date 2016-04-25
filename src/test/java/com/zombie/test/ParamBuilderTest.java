package com.zombie.test;

import com.zombie.bean.GetBrandRequest;
import com.zombie.bean.SimpleBean;
import com.zombie.utils.base.ParamsBuilder;
import com.zombie.utils.base.RandomUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */

@Test
public class ParamBuilderTest {
    Map<String, Object> paramsMap = new HashMap<>();
    GetBrandRequest getBrandRequest;
    SimpleBean simpleBean = new SimpleBean();

    @BeforeClass
    public void setUp() {
        paramsMap.put("Access_token", RandomUtils.generateMixString(20));
        paramsMap.put("data", 100);

        getBrandRequest = new GetBrandRequest();
        List<String> brandList = new ArrayList<>();
        brandList.add("apple");
        brandList.add("google");
        getBrandRequest.setBrand(brandList);
        getBrandRequest.setStart(0);
        getBrandRequest.setEnd(10);

        simpleBean.setName("zombie");
        simpleBean.setAge(30);
    }

    public void buildParam() {
        System.out.println(ParamsBuilder.paramsBuilder("/app/get", paramsMap).toString());
    }

    public void buildWithNullParam() {
        System.out.println(ParamsBuilder.paramsBuilder("/app/get", null).toString());
    }

    public void buildWithBean() {
        System.out.println(ParamsBuilder.paramsBuilder("/app/get", simpleBean).toString());
    }

    public void formData() {
        System.out.println(ParamsBuilder.getFormData(paramsMap));
    }

}
