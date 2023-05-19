package com.learning.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * @ClassName AutoCloseUtil
 * @Description TODO
 * @Author hufei
 * @Date 2023/5/19 19:46
 * @Version 1.0
 */
public class AutoCloseUtil {

    private static final Logger log = LoggerFactory.getLogger(AutoCloseUtil.class);

    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }
}
