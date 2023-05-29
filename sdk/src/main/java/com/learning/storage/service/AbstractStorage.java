package com.learning.storage.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public abstract class AbstractStorage implements Storage {

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
    public void unzip(String relativePath, boolean retain) throws Exception {
        if (!relativePath.endsWith(".zip")) {
            throw new UnsupportedOperationException("invalid file format");
        }
        String relativeDir = relativePath.startsWith("/") ? relativePath.substring(0, relativePath.lastIndexOf("/")) : "";
        try (
                InputStream fileInputStream = getFileStream(relativePath);
                ZipInputStream zipInputStream = new ZipInputStream(fileInputStream, Charset.forName("GBK"))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String filePath = relativeDir + "/" + entry.getName();
                if (!existFile(filePath)) {
                    createFile(filePath);
                }
                writeFile(filePath, zipInputStream, false, false);
                zipInputStream.closeEntry();
            }
        }
        if (!retain) {
            deleteFile(Collections.singletonList(relativePath));
        }
    }

    abstract public void doCreateFile(String relativePath) throws Exception;

    abstract public void doWriteFile(String relativePath, byte[] data, boolean append) throws Exception;

    abstract public void doWriteFile(String relativePath, InputStream stream, boolean append) throws Exception;

    abstract public List<String> doGetFileLines(String relativePath) throws Exception;

    abstract public byte[] doGetFileByte(String relativePath) throws Exception;

    abstract public InputStream doGetFileStream(String relativePath) throws Exception;

    abstract public void doSafeDeleteFile(List<String> relativePaths) throws Exception;
}
