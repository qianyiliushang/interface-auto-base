package com.zombie.utils.encrypt;

public class PKCS7Padding {
    private final static int BLOCK_SIZE = 16;

    public static byte[] getPaddingBytes(int count) {
        int amoutToPad = BLOCK_SIZE - (count % BLOCK_SIZE);
        if (amoutToPad == 0) {
            amoutToPad = BLOCK_SIZE;
        }

        char padChar = chr(amoutToPad);
        String tmp = new String();

        for (int index = 0; index < amoutToPad; index++) {
            tmp += padChar;
        }
        return tmp.getBytes();
    }

    private static char chr(int a) {
        byte target = (byte) a;
        return (char) target;
    }

    public static void main(String[] args) {
        System.out.println(getPaddingBytes(15));
        System.out.println(getPaddingBytes(32));
    }
}
