package com.learning.configuration;

import com.learning.exception.MyException;
import org.springframework.beans.BeansException;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.net.*;
import java.util.Enumeration;
import java.util.Map;

/**
 * @ClassName ServerConfiguration
 * @Description 获取后台服务IP及运行端口号
 * @Author hufei
 * @Date 2020/8/3 15:26
 * @Version 1.0
 */
@Component
public class ServerConfiguration implements ApplicationContextAware, ApplicationListener<WebServerInitializedEvent> {

    private static Environment env;
    private static String address = "";
    private static int port = -1;

    public static String getAddress() {
        return getAddress("HOST_IP");
    }

    public static String getAddress(String addressEnvName) {

        Assert.isTrue(null != addressEnvName && !addressEnvName.isEmpty(), "address env name can not be null");

        if (address.isEmpty()) {
            Map<String, String> envs = System.getenv();
            if (envs.containsKey(addressEnvName)) {
                address = envs.get(addressEnvName);
            } else {
                try {
                    InetAddress addr = InetAddress.getLocalHost();
                    if (null != addr) {
                        address = addr.getHostAddress();
                    }
                } catch (UnknownHostException e) {
                    throw new MyException("获取本地IP出错", e);
                }
            }
            if (null == address || address.equals("127.0.0.1") || address.equals("localhost")) {
                try {
                    Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                    boolean found = false;
                    while (!found && networkInterfaces.hasMoreElements()) {
                        NetworkInterface networkInterface = networkInterfaces.nextElement();
                        Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                        while (!found && inetAddresses.hasMoreElements()) {
                            InetAddress inetAddress = inetAddresses.nextElement();
                            if (inetAddress instanceof Inet4Address && !"127.0.0.1".equals(inetAddress.getHostAddress())) {
                                address = inetAddress.getHostAddress();
                                found = true;
                            }
                        }
                    }
                } catch (SocketException ex) {
                    throw new MyException("获取IP出错", ex);
                }
            }
        }

        return address;
    }

    public static int getPort() {
        return getPort("HOST_PORT");
    }

    public static int getPort(String portEnvName) {

        Assert.isTrue(null != portEnvName && !portEnvName.isEmpty(), "port env name can not be null");

        if (port < 0) {
            Map<String, String> envs = System.getenv();
            if (envs.containsKey(portEnvName)) {
                port = Integer.parseInt(envs.get(portEnvName));
            } else {
                String portProperty = env.getProperty("server.port");
                if (null != portProperty && !portProperty.isEmpty()) {
                    port = Integer.parseInt(portProperty);
                } else {
                    port = 8080;
                }
            }
        }

        return port;
    }

    public static String getAddressLink() {
        return appendAddressAndPort(getAddress(), getPort());
    }

    public static String getAddressLink(String addressEnvName, String portEnvName) {
        return appendAddressAndPort(getAddress(addressEnvName), getPort(portEnvName));
    }

    private static String appendAddressAndPort(String address, int port) {
        Assert.isTrue(!address.isEmpty() && port > 0, "address or port is unavailable");
        return address + ":" + port;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ServerConfiguration.env = applicationContext.getEnvironment();
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        ServerConfiguration.port = event.getWebServer().getPort();
    }
}