package com.learning.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName SocketUtil
 * @Description TODO
 * @Author hufei
 * @Date 2022/11/25 9:44
 * @Version 1.0
 */
public class SocketUtil {

    private static final Logger log = LoggerFactory.getLogger(SocketUtil.class);

    public static String sendMsg(String address, int port, Object obj, boolean withResponse, int timeout) {
        Socket socket = null;
        OutputStream os = null;
        BufferedWriter bw = null;
        InputStream is = null;
        BufferedReader br = null;
        String ret = "";
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(address, port), timeout);
            socket.setSoTimeout(timeout);
            os = socket.getOutputStream();
            bw = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            bw.write(TextUtil.fromJson(obj));
            bw.write("\n");
            bw.flush();
            if (withResponse) {
                //读取服务器返回的消息
                is = socket.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                ret = br.readLine();
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new RuntimeException(address + ":" + port + ", TCP通讯出现错误");
        } finally {
            AutoCloseUtil.close(br);
            AutoCloseUtil.close(is);
            AutoCloseUtil.close(bw);
            AutoCloseUtil.close(os);
            AutoCloseUtil.close(socket);
        }
        return ret;
    }
}
