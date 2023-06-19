package com.learning.utils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @ClassName SSHUtil
 * @Description TODO
 * @Author hufei
 * @Date 2023/6/19 15:42
 * @Version 1.0
 */
public class SSHUtil {

    private static final Logger log = LoggerFactory.getLogger(SSHUtil.class);

    /**
     * @Description 开启服务器连接会话
     * @Param []
     * @Return com.jcraft.jsch.Session
     * @Author hufei
     * @Date 2022/1/20 9:12
     */
    public static Session getSession(String address, String userName, String passWord) {
        Session session = null;
        try {
            //启动会话
            JSch jsch = new JSch();
            session = jsch.getSession(userName, address, 22);
            if (null != passWord && !passWord.isEmpty()) {
                session.setConfig("PreferredAuthentications", "password");
                session.setPassword(passWord);
            }
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(30000);
            session.connect();
            return session;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            closeSession(session);
        }
        return null;
    }

    /**
     * @Description 开启服务器连接会话
     * @Param []
     * @Return com.jcraft.jsch.Session
     * @Author hufei
     * @Date 2022/1/20 9:12
     */
    public static Session getSession(String address, String userName, String prvkey, String pubkey) {
        Session session = null;
        try {
            //启动会话
            JSch jsch = new JSch();
            jsch.addIdentity(prvkey, pubkey, null);
            session = jsch.getSession(userName, address, 22);
            session.setConfig("PreferredAuthentications", "publickey");
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(30000);
            session.connect();
            return session;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            closeSession(session);
        }
        return null;
    }

    /**
     * @Description 关闭服务器连接会话
     * @Param [session]
     * @Return void
     * @Author hufei
     * @Date 2022/1/20 9:13
     */
    public static void closeSession(Session session) {
        if (null != session) {
            session.disconnect();
        }
    }

    /**
     * @Description SFTP上传文件
     * @Param [session, filePath, targetPath]
     * @Return void
     * @Author hufei
     * @Date 2022/1/20 9:21
     */
    public static void sftpUploadFile(Session session, String filePath, String targetPath) {
        if (null != session) {
            ChannelSftp channel = null;
            try {
                //上传文件
                channel = (ChannelSftp) session.openChannel("sftp");
                channel.connect();
                channel.put(filePath, targetPath, ChannelSftp.OVERWRITE);
                channel.quit();
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            } finally {
                if (null != channel) {
                    channel.disconnect();
                }
            }
        }
    }

    /**
     * @Description SHELL执行命令, 无返回值
     * @Param [session, cmd]
     * @Return void
     * @Author hufei
     * @Date 2022/1/20 9:22
     */
    public static void execCommandWithoutReturn(Session session, String cmd) {
        if (null != session) {
            ChannelExec channel = null;
            try {
                //执行命令
                channel = (ChannelExec) session.openChannel("exec");
                channel.setCommand(cmd);
                channel.setInputStream(null);
                channel.setErrStream(null);
                channel.connect();
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            } finally {
                if (null != channel) {
                    channel.disconnect();
                }
            }
        }
    }

    /**
     * @Description SHELL执行命令, 有返回值
     * @Param [session, cmd]
     * @Return void
     * @Author hufei
     * @Date 2022/1/20 9:22
     */
    public static String execCommandWithReturn(Session session, String cmd) {

        StringBuilder builder = new StringBuilder();

        if (null != session) {
            ChannelExec channel = null;
            try {
                //执行命令
                channel = (ChannelExec) session.openChannel("exec");
                channel.setCommand(cmd);
                channel.setInputStream(null);
                channel.setErrStream(System.err, true);
                BufferedReader in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
                channel.connect();
                String msg;
                while ((msg = in.readLine()) != null) {
                    builder.append(msg).append("\n");
                }
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            } finally {
                if (null != channel) {
                    channel.disconnect();
                }
            }
        }

        return builder.toString();
    }
}
