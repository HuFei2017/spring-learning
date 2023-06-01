package com.learning.storage.service;

import com.learning.utils.CollectionUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.*;

public abstract class AbstractStorage implements Storage {

    @Override
    public String uploadFile(String prefix, MultipartFile file) throws Exception {
        return uploadFiles(prefix, new MultipartFile[]{file}).get(0);
    }

    @Override
    public List<String> uploadFiles(String prefix, MultipartFile[] files) throws Exception {

        List<String> fileNameList = new ArrayList<>();

        String pre;
        if (null == prefix || prefix.isEmpty()) {
            pre = "";
        } else {
            if (prefix.startsWith("/")) {
                prefix = prefix.substring(1);
            }
            if (prefix.endsWith("/")) {
                prefix = prefix.substring(0, prefix.length() - 1);
            }
            pre = prefix;
        }

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String filePath = (pre.isEmpty() ? "" : ("/" + pre)) + "/" + fileName;
            if (!existFile(filePath)) {
                createFile(filePath);
            }
            writeFile(filePath, file.getInputStream(), false);
            fileNameList.add(filePath);
        }

        return fileNameList;
    }

    @Override
    public byte[] downloadFile(String relativePath) throws Exception {
        return getFileByte(relativePath);
    }

    @Override
    public void copyFile(String relativePath1, String relativePath2) throws Exception {
        createFile(relativePath2);
        writeFile(relativePath2, getFileStream(relativePath1), false);
    }

    @Override
    public void moveFile(String relativePath1, String relativePath2) throws Exception {
        copyFile(relativePath1, relativePath2);
        deleteFile(Collections.singletonList(relativePath1));
    }

    @Override
    public void createFile(String relativePath) throws Exception {
        if (existFile(relativePath)) {
            throw new FileAlreadyExistsException("file already exist");
        }
        doCreateFile(relativePath);
    }

    @Override
    public void writeFile(String relativePath, String content, boolean append) throws Exception {
        writeFile(relativePath, content.getBytes(StandardCharsets.UTF_8), append);
    }

    @Override
    public void writeFile(String relativePath, byte[] data, boolean append) throws Exception {
        if (!existFile(relativePath)) {
            throw new FileNotFoundException("file not exist");
        }
        doWriteFile(relativePath, data, append);
    }

    @Override
    public void writeFile(String relativePath, InputStream stream, boolean append) throws Exception {
        writeFile(relativePath, stream, append, true);
    }

    private void writeFile(String relativePath, InputStream stream, boolean append, boolean streamOff) throws Exception {
        if (!existFile(relativePath)) {
            throw new FileNotFoundException("file not exist");
        }
        try {
            doWriteFile(relativePath, stream, append);
        } finally {
            if (streamOff) {
                stream.close();
            }
        }
    }

    @Override
    public List<String> getFileLines(String relativePath) throws Exception {
        if (!existFile(relativePath)) {
            throw new FileNotFoundException("file not exist");
        }
        return doGetFileLines(relativePath);
    }

    @Override
    public byte[] getFileByte(String relativePath) throws Exception {
        if (!existFile(relativePath)) {
            throw new FileNotFoundException("file not exist");
        }
        return doGetFileByte(relativePath);
    }

    @Override
    public InputStream getFileStream(String relativePath) throws Exception {
        if (!existFile(relativePath)) {
            throw new FileNotFoundException("file not exist");
        }
        return doGetFileStream(relativePath);
    }

    @Override
    public void deleteFile(List<String> relativePaths) throws Exception {
        for (String relativePath : relativePaths) {
            if (!relativePath.endsWith("/") && !existFile(relativePath)) {
                throw new FileNotFoundException("file not exist");
            }
        }
        safeDeleteFile(relativePaths);
    }

    @Override
    public void safeDeleteFile(List<String> relativePaths) throws Exception {
        doSafeDeleteFile(relativePaths);
    }

    @Override
    public InputStream zip(String relativePath, boolean retain) throws Exception {
        if (!relativePath.endsWith("/")) {
            throw new UnsupportedOperationException("only directory can be zipped");
        }
        if (!relativePath.startsWith("/")) {
            relativePath = "/" + relativePath;
        }
        if (!existFile(relativePath)) {
            throw new FileNotFoundException("file not exist");
        }
        int index = relativePath.length();
        byte[] bytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (CheckedOutputStream cos = new CheckedOutputStream(bos, new CRC32());
                 ZipOutputStream zos = new ZipOutputStream(cos, Charset.forName("GBK"))) {
                doZip(index, Collections.singletonList(relativePath), zos);
            }
            // 在 ZipOutputStream 流关闭之后才能获取到正确的数据
            bytes = bos.toByteArray();
        }
        if (!retain) {
            safeDeleteFile(Collections.singletonList(relativePath));
        }
        return new ByteArrayInputStream(bytes);
    }

    private void doZip(int index, List<String> paths, ZipOutputStream zos) throws Exception {

        List<String> dirPaths = new ArrayList<>();
        List<String> filePaths = new ArrayList<>();

        for (String path : paths) {
            if (path.endsWith("/")) {
                dirPaths.add(path);
            } else {
                filePaths.add(path);
            }
        }

        // 文件
        for (String path : filePaths) {
            String zipPath = path.substring(index);
            zos.putNextEntry(new ZipEntry(zipPath));
            int len;
            byte[] buf = new byte[4096];
            try (InputStream stream = getFileStream(path)) {
                while ((len = stream.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
            }
        }

        // 目录
        for (String path : dirPaths) {
            doZip(index, doListSubFile(path), zos);
        }
    }

    @Override
    public void unzip(String relativePath, boolean retain) throws Exception {
        if (!relativePath.endsWith(".zip")) {
            throw new UnsupportedOperationException("invalid file format");
        }
        String relativeDir;
        if (relativePath.contains("/")) {
            relativeDir = (relativePath.startsWith("/") ? "" : "/") + relativePath.substring(0, relativePath.lastIndexOf("/"));
        } else {
            relativeDir = "";
        }
        try (
                InputStream fileInputStream = getFileStream(relativePath);
                ZipInputStream zipInputStream = new ZipInputStream(fileInputStream, Charset.forName("GBK"))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    String filePath = relativeDir + "/" + entry.getName();
                    if (!existFile(filePath)) {
                        createFile(filePath);
                    }
                    writeFile(filePath, zipInputStream, false, false);
                }
                zipInputStream.closeEntry();
            }
        }
        if (!retain) {
            deleteFile(Collections.singletonList(relativePath));
        }
    }

    @Override
    public void backup(String relativePath, String version, boolean retain) throws Exception {
        if (!relativePath.endsWith("/")) {
            throw new UnsupportedOperationException("only directory can backup");
        }
        if (!relativePath.startsWith("/")) {
            relativePath = "/" + relativePath;
        }
        if (!existFile(relativePath)) {
            throw new FileNotFoundException("file not exist");
        }
        if (relativePath.equals("/")) {
            safeDeleteFile(Collections.singletonList("/bak/"));
        }
        String targetPath = getBackupFilePath(relativePath, version);
        InputStream stream = zip(relativePath, retain);
        if (!existFile(targetPath)) {
            createFile(targetPath);
        }
        writeFile(targetPath, stream, false);
    }

    @Override
    public void rollback(String relativePath, String version, boolean retain) throws Exception {
        if (!relativePath.endsWith("/")) {
            throw new UnsupportedOperationException("only directory can rollback");
        }
        if (!relativePath.startsWith("/")) {
            relativePath = "/" + relativePath;
        }
        String sourcePath = getBackupFilePath(relativePath, version);
        if (!existFile(sourcePath)) {
            throw new FileNotFoundException("no backup file found");
        }
        if (relativePath.equals("/")) {
            List<String> subPaths = doListSubFile("/").stream()
                    .filter(x -> !x.equals("/bak/")).collect(Collectors.toList());
            safeDeleteFile(subPaths);
        } else {
            safeDeleteFile(Collections.singletonList(relativePath));
        }
        String targetPath = relativePath + sourcePath.substring(5);
        copyFile(sourcePath, targetPath);
        unzip(targetPath, false);
        if (!retain) {
            safeDeleteFile(Collections.singletonList(sourcePath));
        }
    }

    @Override
    public void clean(String relativePath) throws Exception {
        if (!relativePath.startsWith("/")) {
            relativePath = "/" + relativePath;
        }
        if (!existFile(relativePath)) {
            throw new FileNotFoundException("file not exist");
        }
        if (relativePath.equals("/")) {
            // 备份以外的目录全部删除
            List<String> subPaths1 = doListSubFile("/").stream()
                    .filter(x -> !x.equals("/bak/")).collect(Collectors.toList());
            safeDeleteFile(subPaths1);
            // 删除备份文件
            String regex = "/bak/bak(_V\\w+)?\\.zip";
            List<String> subPaths2 = doListSubFile("/bak/").stream()
                    .filter(x -> x.matches(regex)).collect(Collectors.toList());
            safeDeleteFile(subPaths2);
        } else {
            // 删除文件夹
            safeDeleteFile(Collections.singletonList(relativePath));
            // 删除备份文件
            String regex = "/bak/bak" + getRelativePathStr(relativePath) + "(_V\\w+)?\\.zip";
            List<String> subPaths2 = doListSubFile("/bak/").stream()
                    .filter(x -> x.matches(regex)).collect(Collectors.toList());
            safeDeleteFile(subPaths2);
        }
    }

    private String getRelativePathStr(String relativePath) {
        return CollectionUtil.join(relativePath.split("/"), "_");
    }

    private String getBackupFilePath(String relativePath, String version) {
        return "/bak/bak" + getRelativePathStr(relativePath) + (null == version || version.isEmpty() ? "" : ("_V" + version)) + ".zip";
    }

    abstract public void doCreateFile(String relativePath) throws Exception;

    abstract public void doWriteFile(String relativePath, byte[] data, boolean append) throws Exception;

    abstract public void doWriteFile(String relativePath, InputStream stream, boolean append) throws Exception;

    abstract public List<String> doGetFileLines(String relativePath) throws Exception;

    abstract public byte[] doGetFileByte(String relativePath) throws Exception;

    abstract public InputStream doGetFileStream(String relativePath) throws Exception;

    abstract public void doSafeDeleteFile(List<String> relativePaths) throws Exception;

    abstract public List<String> doListSubFile(String relativePath) throws Exception;
}
