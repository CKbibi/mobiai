package com.cex0.mobiai.security.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * access token
 * @author Cex0
 * @Data 2020-1-21
 */
@Data
public class AuthToken {

    /**
     * Access token.
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 过期. (秒)
     */
    @JsonProperty("expired_in")
    private int expiredIn;

    /**
     * 刷新令牌。
     */
    @JsonProperty("refresh_token")
    private String refreshToken;
}
