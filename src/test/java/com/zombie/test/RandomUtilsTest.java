package com.zombie.test;

import com.zombie.utils.base.RandomUtils;
import org.testng.Assert;
import org.testng.annotations.Test;



@Test
public class RandomUtilsTest {

    public void randomIntTest() {
        int range = 100;
        int seed = RandomUtils.generateRandomInt(range);
        System.out.println(seed);
        Assert.assertTrue(seed < range);
    }

    public void phoneNumberTest() {
        System.out.println(RandomUtils.getTelNum());
    }
}
