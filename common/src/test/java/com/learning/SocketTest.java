package com.learning;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketTest {

    @Test
    void server() {
        try {
            ServerSocket ss = new ServerSocket(3000);
            System.out.println("启动服务器....");
            Socket s = ss.accept();
            System.out.println("客户端:" + s.getLocalAddress().getHostAddress() + "已连接到服务器");

            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            //读取客户端发送来的消息
            String mess = br.readLine();
            System.out.println("客户端：" + mess);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            bw.write(mess + "\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void client() {
        try {
            Socket s = new Socket("127.0.0.1", 3000);

            //构建IO
            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            //向服务器端发送一条消息
            bw.write("测试客户端和服务器通信，服务器接收到消息返回到客户端\n");
            bw.flush();

            //读取服务器返回的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String mess = br.readLine();
            System.out.println("服务器：" + mess);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
