package com.learning;

import com.learning.utils.compress.ImageUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @ClassName ImageTest
 * @Description TODO
 * @Author hufei
 * @Date 2023/6/14 10:24
 * @Version 1.0
 */
public class ImageTest {

    @Test
    void test() throws Exception {
        FileInputStream inputStream = new FileInputStream(new File("/test.jpg"));
        byte[] compressed = ImageUtil.compress(inputStream.readAllBytes(), 1.5f);
        FileOutputStream outputStream = new FileOutputStream(new File("/test_2.jpg"));
        outputStream.write(compressed);
    }
}
