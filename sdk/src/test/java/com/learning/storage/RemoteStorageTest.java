package com.learning.storage;

import com.learning.storage.configuration.StorageConfig;
import com.learning.storage.configuration.StorageRemoteConfig;
import com.learning.storage.service.Storage;
import com.learning.storage.service.StorageService;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

class RemoteStorageTest {
    @Test
    void remoteStorageTest() throws Exception {

        // mock config
        StorageRemoteConfig remoteConfig = new StorageRemoteConfig();
        remoteConfig.setEndpoint("http://127.0.0.1:9006/");
        remoteConfig.setAccessKey("minio");
        remoteConfig.setSecretKey("minio");
        StorageConfig config = new StorageConfig();
        config.setRemote(remoteConfig);

        // mock autowired
        StorageService storageManager = new StorageService(config);

        // test
        Storage storage = storageManager.build("A");

        String filePath = "/test1";
        String content = "hello";
        storage.createFile(filePath);
        storage.writeFile(filePath, content, false);
        List<String> lines = storage.getFileLines(filePath);
        Assert.isTrue(lines.size() == 1 && lines.get(0).equals(content), "");

        storage.deleteFile(Collections.singletonList(filePath));
        boolean exist = storage.existFile(filePath);
        Assert.isTrue(!exist, "");

        InputStream stream = new FileInputStream("/test.zip");
        String zipName = "abc.zip";
        storage.createFile(zipName);
        storage.writeFile(zipName, stream, false);
        storage.unzip(zipName, false);
        storage.deleteFile(Collections.singletonList("/"));
        Assert.isTrue(!storage.existFile(zipName), "");
    }
}
