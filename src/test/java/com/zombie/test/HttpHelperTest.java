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

        String jsonResonse = HttpHelper.doJsonPost(request, "/api/pb/v1/goods/filterByBrand");
    }

    public void formDataPost() {
        String jsonResonse = HttpHelper.doFormDataPost(request, "/api/pb/v1/goods/filterByBrand");
    }
}
