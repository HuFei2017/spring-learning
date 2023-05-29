package com.learning.storage.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface Storage {

    // 判断文件是否存在
    boolean existFile(String relativePath) throws Exception;

    // 创建空文件
    void createFile(String relativePath) throws Exception;

    // 文件写入
    void writeFile(String relativePath, String content, boolean append) throws Exception;

    // 文件写入
    void writeFile(String relativePath, byte[] data, boolean append) throws Exception;

    // 文件写入
    void writeFile(String relativePath, InputStream stream, boolean append) throws Exception;

    // 获取文件内容, 多行返回
    List<String> getFileLines(String relativePath) throws Exception;

    // 获取文件内容, 二进制流返回
    byte[] getFileByte(String relativePath) throws Exception;

    // 获取文件内容, 输出流
    InputStream getFileStream(String relativePath) throws Exception;

    // 删除文件
    void deleteFile(List<String> relativePaths) throws Exception;

    // 删除文件, 有则删, 无则跳过
    void safeDeleteFile(List<String> relativePaths) throws Exception;

    // 上传文件
    List<String> uploadFiles(String prefix, MultipartFile[] files) throws Exception;

    // 下载文件
    byte[] downloadFile(String relativePath) throws Exception;

    // 复制文件
    void copyFile(String relativePath1, String relativePath2) throws Exception;

    // 移动文件
    void moveFile(String relativePath1, String relativePath2) throws Exception;

    // 解压文件
    void unzip(String relativePath, boolean retain) throws Exception;
}
