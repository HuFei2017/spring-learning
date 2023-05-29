package com.learning.storage.configuration;

import lombok.Data;

@Data
public class StorageRemoteConfig {
    private String endpoint;
    private String accessKey;
    private String secretKey;
}
