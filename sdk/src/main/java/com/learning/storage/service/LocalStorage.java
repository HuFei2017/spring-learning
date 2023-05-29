package com.learning.storage.service;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class LocalStorage extends AbstractStorage {

    private final String[] modelName;
    private final String root;

    public LocalStorage(String root, String... modelName) {
        this.modelName = modelName;
        this.root = root;
    }

    @Override
    public boolean existFile(String relativePath) throws Exception {
        String fullPath = getFullPath(relativePath);
        return Files.exists(Path.of(fullPath));
    }

    @Override
    public void doCreateFile(String relativePath) throws Exception {
        String fullPath = getFullPath(relativePath);
        String fullDir = fullPath.substring(0, fullPath.lastIndexOf("/"));
        if (!fullDir.isEmpty()) {
            Files.createDirectories(Path.of(fullDir));
        }
        Files.createFile(Path.of(fullPath));
    }

    @Override
    public void doWriteFile(String relativePath, byte[] data, boolean append) throws Exception {
        String fullPath = getFullPath(relativePath);
        if (append) {
            Files.write(Path.of(fullPath), data, StandardOpenOption.APPEND);
        } else {
            Files.write(Path.of(fullPath), data);
        }
    }

    @Override
    public void doWriteFile(String relativePath, InputStream stream, boolean append) throws Exception {
        String fullPath = getFullPath(relativePath);
        try (FileOutputStream outputStream = new FileOutputStream(fullPath)) {
            byte[] bytes = new byte[4096];
            int len;
            while ((len = stream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();
        }
    }

    @Override
    public List<String> doGetFileLines(String relativePath) throws Exception {
        String fullPath = getFullPath(relativePath);
        return Files.readAllLines(Path.of(fullPath));
    }

    @Override
    public byte[] doGetFileByte(String relativePath) throws Exception {
        String fullPath = getFullPath(relativePath);
        return Files.readAllBytes(Path.of(fullPath));
    }

    @Override
    public InputStream doGetFileStream(String relativePath) throws Exception {
        String fullPath = getFullPath(relativePath);
        return new FileInputStream(new File(fullPath));
    }

    @Override
    public void doSafeDeleteFile(List<String> relativePaths) throws Exception {
        for (String relativePath : relativePaths) {
            String fullPath = getFullPath(relativePath);
            boolean isDir = Files.isDirectory(Path.of(fullPath));
            if (isDir) {
                Files.walkFileTree(Path.of(fullPath), new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            } else {
                Files.deleteIfExists(Path.of(fullPath));
            }
        }
    }

    private String getFullPath(String relativePath) {
        StringBuilder builder = new StringBuilder(root);
        if (null != modelName) {
            for (String modelItem : modelName) {
                builder.append("/").append(modelItem);
            }
        }
        if (!relativePath.startsWith("/")) {
            builder.append("/");
        }
        builder.append(relativePath);
        return builder.toString();
    }
}
