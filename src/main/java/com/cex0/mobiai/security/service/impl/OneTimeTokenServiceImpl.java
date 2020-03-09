package com.cex0.mobiai.security.service.impl;

import com.cex0.mobiai.cache.StringCacheStore;
import com.cex0.mobiai.security.service.OneTimeTokenService;
import com.cex0.mobiai.util.MobiaiUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/9 23:15
 * @Description: 一次性令牌接口实现。
 */
@Service
public class OneTimeTokenServiceImpl implements OneTimeTokenService {

    /**
     * 一次性token过期时间（单位：天）
     */
    private static final int OTT_EXPIRED_DAY = 1;

    private final StringCacheStore cacheStore;

    public OneTimeTokenServiceImpl(StringCacheStore cacheStore) {
        this.cacheStore = cacheStore;
    }

    @Override
    public Optional<String> get(String oneTimeToken) {
        Assert.hasText(oneTimeToken, "One-time token must not be blank");

        // 从缓存池中获取
        return cacheStore.get(oneTimeToken);
    }

    @Override
    public String create(String uri) {
        Assert.hasText(uri, "Request uri must not be blank");

        // 生成token
        String oneTimeToken = MobiaiUtils.randomUUIDWithoutDash();

        cacheStore.put(oneTimeToken, uri, OTT_EXPIRED_DAY, TimeUnit.DAYS);

        return oneTimeToken;
    }

    @Override
    public void revoke(String oneTimeToken) {
        Assert.hasText(oneTimeToken, "One-time token must not be blank");

        // 删除token
        cacheStore.delete(oneTimeToken);
    }
}
