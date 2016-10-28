package com.zombie;

import com.zombie.utils.base.RandomUtils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 *
 */

public class DataPrepare {
    public static void main(String[] args) {
        try {
            PrintWriter printWriter = new PrintWriter("data.text");
            for (int i = 0; i < 200; i++) {
              // printWriter.write(RandomUtils.getRandomJianHan(3));
                printWriter.write(RandomUtils.generateLowerString(8));
                printWriter.write("\n");
            }
            printWriter.flush();
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
