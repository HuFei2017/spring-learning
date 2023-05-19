package com.learning.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName ByteUtil
 * @Description TODO
 * @Author jiashudong
 * @Date 2021/11/7 20:55
 * @Version 1.0
 */
public class ByteUtil {

    private static final Logger log = LoggerFactory.getLogger(ByteUtil.class);

    //将byte[]转换成int
    public static int getByteConvertInt(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < bytes.length; i++) {
            value |= ((bytes[i] & 0xff) << (i * 8));

        }
        return value;
    }

    //将byte[]转换成float
    public static float getByteConvertFloat(byte[] bytes) {
        return Float.intBitsToFloat(getByteConvertInt(bytes));
    }

    //将byte[]转换成long
    public static long getByteConvertLong(byte[] bytes) {
        long value = 0;
        for (int i = 0; i < bytes.length; i++) {
            // 需要转long再位移，否则int丢失精度
            value |= ((long) (bytes[i] & 0xff) << (i * 8));
        }
        return value;
    }

    //将byte[]转换成double
    public static double getByteConvertDouble(byte[] bytes) {
        return Double.longBitsToDouble(getByteConvertLong(bytes));
    }

    //将byte[]转换成String
    public static String getByteConvertString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    //将int转换成byte[]
    public static byte[] getIntConvertByte(int data) {
        byte[] array = new byte[4];
        for (int i = 0; i < array.length; i++) {
            array[i] = (byte) (data >> (i * 8));
        }
        return array;
    }

    //将float转换成byte[]
    public static byte[] getFloatConvertByte(float data) {
        return getIntConvertByte(Float.floatToIntBits(data));
    }

    //将long转换成byte[]
    public static byte[] getLongConvertByte(long data) {
        byte[] array = new byte[8];
        for (int i = 0; i < array.length; i++) {
            array[i] = (byte) (data >> (i * 8));
        }
        return array;
    }

    //将double转换成byte[]
    public static byte[] getDoubleConvertByte(double data) {
        return getLongConvertByte(Double.doubleToLongBits(data));
    }

    //将String转换成byte[]
    public static byte[] getStringConvertByte(String data) {
        return data.getBytes(StandardCharsets.UTF_8);
    }
}
