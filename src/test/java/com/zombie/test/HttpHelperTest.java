package com.zombie.test;

import com.zombie.http.HttpHelper;
import org.testng.annotations.Test;

/**
 *
 */

@Test
public class HttpHelperTest {
    String request = "{" +
            "    \"brand\":[\"apple\",\"google\"]," +
            "    \"start\":0," +
            "    \"end\":9" +
            "}";


    public void jsonPostTest() {
        // request = FastJsonUtil.objectToJsonString(request);
        //System.out.println(request);
        //System.out.println(GsonUtils.formatJsonString(request));
        String jsonResonse = HttpHelper.doJsonPost(request, "/api/pb/v1/goods/filterByBrand");
    }
}
