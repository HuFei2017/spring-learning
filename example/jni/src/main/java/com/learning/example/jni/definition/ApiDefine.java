package com.learning.example.jni.definition;

import com.sun.jna.Library;

/**
 * @InterfaceName DllDefine
 * @Description TODO
 * @Author hufei
 * @Date 2022/11/11 19:55
 * @Version 1.0
 */
public interface ApiDefine extends Library {
    double demohu(double a, double b);
}
