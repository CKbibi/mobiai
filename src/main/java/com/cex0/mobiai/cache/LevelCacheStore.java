package com.cex0.mobiai.cache;

import com.cex0.mobiai.config.properties.MobiaiProperties;
import com.cex0.mobiai.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.iq80.leveldb.*;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/9 21:17
 * @Description: Level-DB作为缓存
 */
@Slf4j
public class LevelCacheStore extends StringCacheStore {

    /**
     * 计划清除时间（毫秒）
     */
    private final static long PERIOD = 60 * 1000;

    private static DB LEVEL_DB;

    private Timer timer;

    @Autowired
    private MobiaiProperties properties;

    @PostConstruct
    public void init() {
        if (LEVEL_DB != null) { return;}
        try {
            File folder = new File(properties.getWorkDir() + ".level.db");
            DBFactory factory = new Iq80DBFactory();
            Options options = new Options();
            options.createIfMissing(true);
            // 打开level-db存储文件夹
            LEVEL_DB = factory.open(folder, options);
            timer = new Timer();
            timer.scheduleAtFixedRate(new CacheExpiryCleaner(), 0, PERIOD);
        }
        catch (Exception e) {
            log.error("init level-db error", e);
        }
    }

    /**
     * 销毁
     */
    @PreDestroy
    public void preDestroy() {
        try {
            LEVEL_DB.close();
            timer.cancel();
        }
        catch (IOException e) {
            log.error("close level-db error", e);
        }
    }


    @Override
    Optional<CacheWrapper<String>> getInternal(String key) {
        Assert.hasText(key, "Cache key must not be blank");
        byte[] bytes = LEVEL_DB.get(stringToBytes(key));
        if (bytes != null) {
            String valueJson = bytesToString(bytes);
            return StringUtils.isEmpty(valueJson) ? Optional.empty() : jsonToCacheWrapper(valueJson);
        }
        return Optional.empty();
    }

    @Override
    void putInternal(String key, CacheWrapper<String> cacheWrapper) {
        putInternalIfAbsent(key, cacheWrapper);
    }

    @Override
    Boolean putInternalIfAbsent(String key, CacheWrapper<String> cacheWrapper) {
        Assert.hasText(key, "Cache key must not be blank");
        Assert.notNull(cacheWrapper, "Cache Wrapper must not be null");

        try {
            LEVEL_DB.put(stringToBytes(key), stringToBytes(JsonUtils.objectToJson(cacheWrapper)));
            return true;
        }
        catch (JsonProcessingException e) {
            log.warn("Put cache fail json2object key: [{}] value: [{}]", key, cacheWrapper);
        }
        log.debug("Cache key: [{}], original cache wrapper: [{}]", key, cacheWrapper);
        return false;
    }

    @Override
    public void delete(String key) {
        LEVEL_DB.delete(stringToBytes(key));
        log.debug("Cache remove key: [{}]", key);
    }

    @NonNull
    private byte[] stringToBytes(@NonNull String str) {
        return str.getBytes(Charset.defaultCharset());
    }

    @NonNull
    private String bytesToString(@NonNull byte[] bytes) {
        return new String(bytes, Charset.defaultCharset());
    }

    private Optional<CacheWrapper<String>> jsonToCacheWrapper(String json) {
        Assert.hasText(json, "Json value must not be blank");
        CacheWrapper<String> cacheWrapper = null;

        try {
            cacheWrapper = JsonUtils.jsonToObject(json, CacheWrapper.class);
        }
        catch (IOException e) {
            e.printStackTrace();
            log.debug("error json to wrapper value bytes: [{}]", json, e);
        }
        return Optional.ofNullable(cacheWrapper);
    }

    private class CacheExpiryCleaner extends TimerTask {

        @Override
        public void run() {
            // 批量
            WriteBatch writeBatch = LEVEL_DB.createWriteBatch();

            DBIterator iterator = LEVEL_DB.iterator();
            long currentTimeMillis = System.currentTimeMillis();
            while (iterator.hasNext()) {
                Map.Entry<byte[], byte[]> next = iterator.next();
                if (next.getKey() == null || next.getValue() == null) {
                    continue;
                }

                String valueJson = bytesToString(next.getValue());
                Optional<CacheWrapper<String>> stringCacheWrapper = StringUtils.isEmpty(valueJson) ?
                        Optional.empty() : jsonToCacheWrapper(valueJson);

                if (stringCacheWrapper.isPresent()) {
                    // 获取过期时间
                    Long expireAtTime = stringCacheWrapper.map(CacheWrapper::getExpireAt)
                            .map(Date::getTime)
                            .orElse(0L);
                    // 如果过期
                    if (expireAtTime != 0 && currentTimeMillis > expireAtTime) {
                        writeBatch.delete(next.getKey());
                        log.debug("delete the cache: [{}] for expiration", bytesToString(next.getKey()));
                    }
                }
            }
            LEVEL_DB.write(writeBatch);
        }
    }
}
