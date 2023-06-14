package com.learning.utils.compress;

import com.learning.utils.AutoCloseUtil;
import net.coobird.thumbnailator.Thumbnails;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName ImageUtil
 * @Description TODO
 * @Author hufei
 * @Date 2023/6/14 10:10
 * @Version 1.0
 */
public class ImageUtil {
    public static byte[] compress(byte[] image, double scale) {
        if (scale < 0) {
            return image;
        }
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            inputStream = new ByteArrayInputStream(image);
            outputStream = new ByteArrayOutputStream();
            Thumbnails.of(inputStream).scale(scale).toOutputStream(outputStream);
            return outputStream.toByteArray();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            AutoCloseUtil.close(outputStream);
            AutoCloseUtil.close(inputStream);
        }
        return image;
    }
}
