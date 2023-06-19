package com.learning;

import com.jcraft.jsch.Session;
import com.learning.utils.SSHUtil;
import org.junit.jupiter.api.Test;

/**
 * @ClassName SSHTest
 * @Description TODO
 * @Author hufei
 * @Date 2023/6/19 15:45
 * @Version 1.0
 */
public class SSHTest {
    @Test
    void test() {
        Session session = SSHUtil.getSession("localhost", "username", "password");
        System.out.println(SSHUtil.execCommandWithReturn(session, "ip addr"));
        SSHUtil.closeSession(session);
    }
}
