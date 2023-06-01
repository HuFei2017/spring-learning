package com.learning.storage.service;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RemoteStorage extends LocalStorage {

    private final String BUCKET_NAME = "phm-bucket";
    private final String[] modelName;
    private final MinioClient client;
    private boolean bucketOn = false;

    public RemoteStorage(MinioClient client, String... modelName) {
        super("/tmp", modelName);
        this.modelName = modelName;
        this.client = client;
    }

    @Override
    public boolean existFile(String relativePath) throws Exception {
        if (!bucketOn) {
            checkBucket();
        }
        if (relativePath.endsWith("/")) {
            return true;
        }
        try {
            client.statObject(
                    StatObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(getFullPath(relativePath))
                            .build()
            );
        } catch (ErrorResponseException ex) {
            if (ex.errorResponse().code().equals("NoSuchKey")) {
                return false;
            } else {
                throw ex;
            }
        }
        return true;
    }

    @Override
    public void doCreateFile(String relativePath) throws Exception {
        if (!bucketOn) {
            checkBucket();
        }
        client.putObject(
                PutObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(getFullPath(relativePath))
                        .stream(
                                new ByteArrayInputStream(new byte[]{}), 0, -1
                        )
                        .build()
        );
    }

    @Override
    public void doWriteFile(String relativePath, byte[] data, boolean append) throws Exception {
        if (!bucketOn) {
            checkBucket();
        }
        client.putObject(
                PutObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(getFullPath(relativePath))
                        .stream(
                                new ByteArrayInputStream(data), data.length, -1
                        )
                        .build()
        );
    }

    @Override
    public void doWriteFile(String relativePath, InputStream stream, boolean append) throws Exception {
        if (!bucketOn) {
            checkBucket();
        }
        client.putObject(
                PutObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(getFullPath(relativePath))
                        .stream(
                                stream, -1, 10485760
                        )
                        .build()
        );
    }

    @Override
    public List<String> doGetFileLines(String relativePath) throws Exception {
        try (
                InputStream stream = doGetFileStream(relativePath);
                InputStreamReader streamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader)
        ) {
            List<String> result = new ArrayList<>();
            for (; ; ) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                result.add(line);
            }
            return result;
        }
    }

    @Override
    public byte[] doGetFileByte(String relativePath) throws Exception {
        if (!bucketOn) {
            checkBucket();
        }
        try (GetObjectResponse response = client.getObject(
                GetObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(getFullPath(relativePath))
                        .build()
        )) {
            return response.readAllBytes();
        }
    }

    @Override
    public InputStream doGetFileStream(String relativePath) throws Exception {
        if (!bucketOn) {
            checkBucket();
        }
        return client.getObject(
                GetObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(getFullPath(relativePath))
                        .build()
        );
    }

    @Override
    public void doSafeDeleteFile(List<String> relativePaths) throws Exception {
        if (!bucketOn) {
            checkBucket();
        }
        List<String> fullPathList = new ArrayList<>();
        for (String relativePath : relativePaths) {
            String fullPath = getFullPath(relativePath);
            if (fullPath.endsWith("/")) {
                fullPathList.addAll(listDir(fullPath));
            } else {
                fullPathList.add(fullPath);
            }
        }
        List<DeleteObject> objects = new ArrayList<>();
        for (String fullPath : fullPathList) {
            objects.add(new DeleteObject(fullPath));
        }
        Iterable<Result<DeleteError>> results =
                client.removeObjects(
                        RemoveObjectsArgs.builder()
                                .bucket(BUCKET_NAME)
                                .objects(objects)
                                .build()
                );
        for (Result<DeleteError> result : results) {
            result.get();
        }
    }

    @Override
    public List<String> doListSubFile(String relativePath) throws Exception {
        if (!bucketOn) {
            checkBucket();
        }
        List<String> list = new ArrayList<>();
        String fullPath = getFullPath(relativePath);
        if (fullPath.endsWith("/")) {
            Iterable<Result<Item>> results = client.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(BUCKET_NAME)
                            .prefix(fullPath)
                            .build()
            );
            int refIndex = getFullPath("/").length() + relativePath.length() - 1;
            for (Result<Item> result : results) {
                String name = result.get().objectName().substring(refIndex);
                list.add(relativePath + name);
            }
        }
        return list;
    }

    private List<String> listDir(String dir) throws Exception {
        List<String> list = new ArrayList<>();
        if (dir.endsWith("/")) {
            Iterable<Result<Item>> results = client.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(BUCKET_NAME)
                            .prefix(dir)
                            .build()
            );
            for (Result<Item> result : results) {
                String name = result.get().objectName();
                if (name.endsWith("/")) {
                    list.addAll(listDir(name));
                } else {
                    list.add(name);
                }
            }
        }
        return list;
    }

    private String getFullPath(String relativePath) {
        StringBuilder builder = new StringBuilder();
        if (null != modelName) {
            for (String modelItem : modelName) {
                builder.append("/").append(modelItem);
            }
        }
        if (!relativePath.startsWith("/")) {
            builder.append("/");
        }
        builder.append(relativePath);
        return builder.substring(1);
    }

    private void checkBucket() throws Exception {
        boolean hasBucket = client.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());
        if (!hasBucket) {
            client.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
            bucketOn = true;
        }
    }
}
