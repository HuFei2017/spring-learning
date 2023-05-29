package com.learning.storage.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "storage")
public class StorageConfig {
    private StorageLocalConfig local;
    private StorageRemoteConfig remote;
}
