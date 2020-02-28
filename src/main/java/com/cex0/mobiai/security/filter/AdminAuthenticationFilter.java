package com.cex0.mobiai.security.filter;

import com.cex0.mobiai.config.properties.MobiaiProperties;
import com.cex0.mobiai.service.OptionService;
import com.cex0.mobiai.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

/**
 * 管理身份验证筛选器。
 *
 * @author Cex0
 * @date 2020/02/28
 */
@Slf4j
public class AdminAuthenticationFilter extends AbstractAuthenticationFilter {

    /**
     * session key
     */
    public final static String ADMIN_SESSION_KEY = "mobiai.admin.session";

    /**
     *  令牌访问缓存前缀
     */
    public final static String TOKEN_ACCESS_CACHE_PREFIX = "mobiai.admin.access.token.";

    /**
     * 令牌刷新缓存前缀
     */
    public final static String TOKEN_REFRESH_CACHE_PREFIX = "mobiai.admin.refresh.token.";

    /**
     * token标题名。
     */
    public final static String ADMIN_TOKEN_HEADER_NAME = "ADMIN-" + HttpHeaders.AUTHORIZATION;

    /**
     *  管理令牌查询名称
     */
    public final static String ADMIN_TOKEN_QUERY_NAME = "admin_token";

//    private final MobiaiProperties mobiaiProperties;
//
//    //缓存部分
////    private final StringCacheStore cacheStore;
//
//    private final UserService userService;
//
//    public AdminAuthenticationFilter(StringCacheStore cacheStore,
//                                     UserService userService,
//                                     HaloProperties haloProperties,
//                                     OptionService optionService) {
//        super(haloProperties, optionService);
//        this.cacheStore = cacheStore;
//        this.userService = userService;
//        this.haloProperties = haloProperties;
}
