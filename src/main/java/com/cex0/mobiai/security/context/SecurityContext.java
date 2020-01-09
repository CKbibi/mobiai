package com.cex0.mobiai.security.context;

import com.cex0.mobiai.security.authentication.Authentication;
import org.springframework.lang.NonNull;

/**
 *  上下文安全接口
 *  @author Cex0
 */
public interface SecurityContext {

    /**
     * 获取当前经过身份验证的主体
     * @return 如果身份验证信息不可用，返回null
     */
    @NonNull
    Authentication getAuthentication();


    /**
     * 更改当前已验证的主体，或删除验证信息。
     * @param authentication 新的身份验证或者空的身份验证，如果没有进一步的身份验证不应该存储
     */
    void setAuthentication(Authentication authentication);

    default boolean isAuthenticated() {
        return getAuthentication() != null;
    }
}
