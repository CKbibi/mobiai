package com.cex0.mobiai.security.service;

import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/9 23:10
 * @Description: 一次性令牌接口。
 */
public interface OneTimeTokenService {

    /**
     * 获取相应的uri。
     *
     * @param oneTimeToken
     * @return
     */
    @NonNull
    Optional<String> get(@NonNull String oneTimeToken);

    /**
     * 创建一次性token
     *
     * @param uri
     * @return
     */
    @NonNull
    String create(@NonNull String uri);

    /**
     * 撤回一次性token
     *
     * @param oneTimeToken
     */
    @NonNull
    void revoke(@NonNull String oneTimeToken);
}
