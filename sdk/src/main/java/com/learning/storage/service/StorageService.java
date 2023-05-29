package com.learning.storage.service;

import com.learning.storage.configuration.StorageConfig;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 存储通用服务
 * 使用方式:
 * 1. 注入 StorageService storageService
 * 2. 操作文件
 */
@Component
public class StorageService {

    private final StorageConfig config;
    private final boolean canRemote;
    private MinioClient client = null;
    private boolean clientFailed = false;

    private final Logger log = LoggerFactory.getLogger(StorageService.class);

    public StorageService(StorageConfig config) {
        this.config = config;
        this.canRemote = null != config && null != config.getRemote() && null != config.getRemote().getEndpoint() && !config.getRemote().getEndpoint().isEmpty();
    }

    public Storage build(String... modelName) {

        if (!canRemote) {
            return new LocalStorage(getRootDir(), modelName);
        }

        if (null == client) {
            synchronized (this) {
                if (!clientFailed && null == client) {
                    try {
                        client = MinioClient.builder()
                                .endpoint(config.getRemote().getEndpoint())
                                .credentials(config.getRemote().getAccessKey(), config.getRemote().getSecretKey())
                                .build();
                    } catch (Exception ex) {
                        log.error("远程存储服务启动, Minio连接失败", ex);
                        clientFailed = true;
                    }
                }
            }
        }

        return null == client ? new LocalStorage(getRootDir(), modelName) : new RemoteStorage(client, modelName);
    }

    private String getRootDir() {
        if (null == config || null == config.getLocal() || null == config.getLocal().getRoot() || config.getLocal().getRoot().isEmpty()) {
            return "";
        } else {
            String configRoot = config.getLocal().getRoot();
            return configRoot.endsWith("/") ? configRoot.substring(0, configRoot.length() - 1) : configRoot;
        }
    }
}
