package com.learning.example.jni;

import com.learning.example.jni.definition.ApiDefine;
import com.sun.jna.Native;
import org.junit.jupiter.api.Test;

public class ApiTest {

    @Test
    void dllTest() {
        ApiDefine define1 = Native.load("C:\\test\\demo.dll", ApiDefine.class);
        double ret1 = define1.demohu(3.0, 2.0);
        System.out.println(ret1);
    }

    @Test
    void soTest() {
        ApiDefine define1 = Native.load("C:\\test\\demo.so", ApiDefine.class);
        double ret1 = define1.demohu(3.0, 2.0);
        System.out.println(ret1);
    }

}
