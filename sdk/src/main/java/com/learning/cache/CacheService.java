package com.learning.cache;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.template.QuickConfig;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.Duration;

/**
 * 缓存通用服务
 * 使用方式:
 * 1. 创建变量 xxCache
 * 2. 注入 CacheService cacheService
 * 3. xxCache赋值, 确定是否需要远程缓存, 以及缓存时长, 示例:
 * private final Cache<K, V> xxCache;
 * public xxService(CacheService cacheService){
 * this.xxCache = cacheService.build("xxCache", true, 600);
 * }
 */
@Component
public class CacheService {

    private final CacheManager cacheManager;
    private final boolean canRemote;

    public CacheService(CacheManager cacheManager,
                        ConfigurableEnvironment environment) {
        this.cacheManager = cacheManager;
        this.canRemote = null != environment.getProperty("jetcache.remote.default.type");
    }

    public <K, V> Cache<K, V> build(String name, boolean local, long duration) {
        Assert.isTrue(null != name && !name.isEmpty(), "name can not be null");
        Assert.isTrue(duration > 0, "duration must bigger than 0");
        QuickConfig qc = QuickConfig.newBuilder(name)
                .expire(Duration.ofSeconds(duration))
                .cacheType(
                        canRemote ? (local ? CacheType.LOCAL : CacheType.REMOTE) : CacheType.LOCAL
                )
                .syncLocal(true) // invalidate local cache in all jvm process after update
                .build();
        return cacheManager.getOrCreateCache(qc);
    }

}
