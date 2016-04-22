package com.zombie.test;

import com.zombie.utils.base.RandomUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

/**
 *
 */

public class HeaderTest {
    public static void main(String[] args) {
        Header header = new BasicHeader("token", RandomUtils.generateMixString(20));
        System.out.println(header);
    }
}
