package com.zombie.utils.encrypt;

import com.zombie.utils.base.RandomUtils;

import java.io.UnsupportedEncodingException;

/**
 * Base64编码,解码
 */

public class Base64Util {
    public static final String DEFAULT_ENCODING = "UTF-8";

    public static String encode(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("byte data cannot be null");
        }

        return encode(bytes, DEFAULT_ENCODING);
    }

    public static String encode(byte[] bytes, String encoding) {
        if (bytes == null) {
            throw new IllegalArgumentException("byte data cannot be null");
        }
        String result;
        try {
            result = new String(_encode(bytes), encoding);
            return result;
        } catch (UnsupportedEncodingException e) {
            return null;
        }

    }

    public static String encode(String src) {
        return new String(encode(src, DEFAULT_ENCODING));
    }

    public static byte[] encode(String src, String encoding) {
        if (src == null) {
            throw new IllegalArgumentException("byte data cannot be null");
        }
        byte[] result;
        try {
            result = _encode(src.getBytes(encoding));
            return result;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private final static byte[] _encode(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("byte data cannot be null");
        }

        /**
         * 编码后的的数组比输入数据大1/3,因为每组3个字节被编码成4个字节
         */
        int srcIdx;
        int dstIdx;
        byte[] destByte = new byte[((bytes.length + 2) / 3) * 4];


        /**
         * 遍历输入数组,一次24位,并将它们从3组8位数据转换成4组6位(其中有2位未设置),参考BASE64维基百科
         * https://zh.wikipedia.org/wiki/Base64
         */
        for (srcIdx = 0, dstIdx = 0; srcIdx < bytes.length - 2; srcIdx += 3) {
            destByte[dstIdx++] = (byte) ((bytes[srcIdx] >>> 2) & 077);
            destByte[dstIdx++] = (byte) ((bytes[srcIdx + 1] >>> 4) & 017 | (bytes[srcIdx] << 4) & 077);
            destByte[dstIdx++] = (byte) ((bytes[srcIdx + 2] >>> 6) & 003 | (bytes[srcIdx + 1] << 2) & 077);
            destByte[dstIdx++] = (byte) (bytes[srcIdx + 2] & 077);
        }

        /**
         * 如果要编码的字节数不能被3整除，处理多出的1个或2个字节
         */
        if (srcIdx < bytes.length) {
            destByte[dstIdx++] = (byte) ((bytes[srcIdx] >>> 2) & 077);
            if (srcIdx < bytes.length - 1) {
                destByte[dstIdx++] = (byte) ((bytes[srcIdx + 1] >>> 4) & 017 | (bytes[srcIdx] << 4) & 077);
                destByte[dstIdx++] = (byte) ((bytes[srcIdx + 1] << 2) & 077);
            } else
                destByte[dstIdx++] = (byte) ((bytes[srcIdx] << 4) & 077);
        }

        /**
         * 将编码后的字节转换成字符
         */
        for (srcIdx = 0; srcIdx < dstIdx; srcIdx++) {
            if (destByte[srcIdx] < 26) {
                destByte[srcIdx] = (byte) (destByte[srcIdx] + 'A');
            } else if (destByte[srcIdx] < 52) {
                destByte[srcIdx] = (byte) (destByte[srcIdx] + 'a' - 26);
            } else if (destByte[srcIdx] < 62) {
                destByte[srcIdx] = (byte) (destByte[srcIdx] + '0' - 52);
            } else if (destByte[srcIdx] < 63) {
                destByte[srcIdx] = '+';
            } else {
                destByte[srcIdx] = '/';
            }

        }

        /**
         * 将未使用的字符转换成'='
         */
        for (; srcIdx < destByte.length; srcIdx++) {
            destByte[srcIdx] = '=';
        }
        return destByte;
    }

    public static String decode(byte[] encoded) throws UnsupportedEncodingException {
        return new String(_decode(encoded), DEFAULT_ENCODING);
    }

    public static String decode(byte[] encoded, String encoding) throws UnsupportedEncodingException {
        return new String(_decode(encoded), encoding);
    }

    public static String decodeString(String encoded, String encoding) {
        if (null == encoded) {
            throw new IllegalArgumentException("encoded cannot be null");
        }
        try {
            byte[] bytes = encoded.getBytes(encoding);
            return decode(bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String decodeString(String encoded) {
        if (null == encoded) {
            throw new IllegalArgumentException("encoded cannot be null");
        }
        try {
            byte[] bytes = encoded.getBytes(DEFAULT_ENCODING);
            return decode(bytes, DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] decode(String encoded) {
        return decode(encoded, DEFAULT_ENCODING);
    }

    public static byte[] decode(String encoded, String encoding) {
        if (null == encoded) {
            throw new IllegalArgumentException("encoded cannot be null");
        }
        byte[] bytes;
        try {
            bytes = _decode(encoded.getBytes(encoding));
            return bytes;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private static byte[] _decode(byte[] byteData) {
        if (byteData == null) {
            throw new IllegalArgumentException(" bytes data cannot be null");
        }
        /**
         * 解码得到的数组大约是原输入数据的3/4大小,因为将每组4字节编码成3字节每组
         */
        int iSrcIdx; // index into source (byteData)
        int reviSrcIdx; // index from end of the src array (byteData)
        int iDestIdx; // index into destination (byteDest)
        byte[] byteTemp = new byte[byteData.length];

        /**
         * 删除所有补位的"=",实际上并不用删除,只需要得到数组结束的真实索引即可
         */
        for (reviSrcIdx = byteData.length; reviSrcIdx - 1 > 0 && byteData[reviSrcIdx - 1] == '='; reviSrcIdx--) {
        }

        /**
         * 如果输入全都是占位符"="
         */
        if (reviSrcIdx - 1 == 0) {
            return null;
        }

        byte byteDest[] = new byte[((reviSrcIdx * 3) / 4)];

        for (iSrcIdx = 0; iSrcIdx < reviSrcIdx; iSrcIdx++) {
            if (byteData[iSrcIdx] == '+')
                byteTemp[iSrcIdx] = 62;
            else if (byteData[iSrcIdx] == '/')
                byteTemp[iSrcIdx] = 63;
            else if (byteData[iSrcIdx] < '0' + 10)
                byteTemp[iSrcIdx] = (byte) (byteData[iSrcIdx] + 52 - '0');
            else if (byteData[iSrcIdx] < ('A' + 26))
                byteTemp[iSrcIdx] = (byte) (byteData[iSrcIdx] - 'A');
            else if (byteData[iSrcIdx] < 'a' + 26)
                byteTemp[iSrcIdx] = (byte) (byteData[iSrcIdx] + 26 - 'a');
        }

        for (iSrcIdx = 0, iDestIdx = 0; iSrcIdx < reviSrcIdx
                && iDestIdx < ((byteDest.length / 3) * 3); iSrcIdx += 4) {
            byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx] << 2) & 0xFC | (byteTemp[iSrcIdx + 1] >>> 4) & 0x03);
            byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx + 1] << 4) & 0xF0 | (byteTemp[iSrcIdx + 2] >>> 2) & 0x0F);
            byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx + 2] << 6) & 0xC0 | byteTemp[iSrcIdx + 3] & 0x3F);
        }

        if (iSrcIdx < reviSrcIdx) {
            if (iSrcIdx < reviSrcIdx - 2) {
                // "3 input bytes left"
                byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx] << 2) & 0xFC | (byteTemp[iSrcIdx + 1] >>> 4) & 0x03);
                byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx + 1] << 4) & 0xF0 | (byteTemp[iSrcIdx + 2] >>> 2) & 0x0F);
            } else if (iSrcIdx < reviSrcIdx - 1) {
                // "2 input bytes left"
                byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx] << 2) & 0xFC | (byteTemp[iSrcIdx + 1] >>> 4) & 0x03);
            } else {
                throw new IllegalArgumentException("Warning: 1 input bytes left to process. This was not Base64 input");
            }
        }
        return byteDest;

    }

    public static void main(String[] args) {
        String sss = "NThjYjM4NGQxMGMyMjM5YTg1MTc3YjE1OTY1N2Y3YjcyZDMzZjg5M2QwMDI5OGE3MmJkNDUzM2NlMzgyYTU2NQ==";
        //System.out.println(sss);
       // String encoded = encode(sss);
       // System.out.println(encoded);
        System.out.println(decodeString(sss));
       // System.out.println(decodeString("NThjYjM4NGQxMGMyMjM5YTg1MTc3YjE1OTY1N2Y3YjcyZDMzZjg5M2QwMDI5OGE3MmJkNDUzM2NlMzgyYTU2NQ=="));
    }
}
