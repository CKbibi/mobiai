package com.cex0.mobiai.config;

import com.cex0.mobiai.cache.InMemoryCacheStore;
import com.cex0.mobiai.cache.LevelCacheStore;
import com.cex0.mobiai.cache.StringCacheStore;
import com.cex0.mobiai.config.properties.MobiaiProperties;
import com.cex0.mobiai.filter.CorsFilter;
import com.cex0.mobiai.filter.LogFilter;
import com.cex0.mobiai.security.filter.AdminAuthenticationFilter;
import com.cex0.mobiai.security.filter.ApiAuthenticationFilter;
import com.cex0.mobiai.security.filter.ContentFilter;
import com.cex0.mobiai.security.handler.ContentAuthenticationFailureHandler;
import com.cex0.mobiai.security.handler.DefaultAuthenticationFailureHandler;
import com.cex0.mobiai.security.service.OneTimeTokenService;
import com.cex0.mobiai.service.OptionService;
import com.cex0.mobiai.service.UserService;
import com.cex0.mobiai.util.HttpClientUtils;
import com.cex0.mobiai.util.MobiaiUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * mobiai configuration.
 * @author wodenvyoujiaoshaxiong
 */
@Configuration
@EnableConfigurationProperties(MobiaiProperties.class)
@Slf4j
public class MobiaiConfiguration {

    @Autowired
    private MobiaiProperties mobiaiProperties;

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        builder.failOnUnknownProperties(false);
        return builder.build();
    }

    @Bean
    public RestTemplate httpsRestTemplate(RestTemplateBuilder builder)
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        RestTemplate httpsRestTemplate = builder.build();
        httpsRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpClientUtils.createHttpsClient(
                (int) mobiaiProperties.getDownloadTimeout().toMillis())));
        return httpsRestTemplate;
    }


    /**
     * 初始化缓存池
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public StringCacheStore stringCacheStore() {
        StringCacheStore stringCacheStore;
        switch (mobiaiProperties.getCache()) {
            case "level":
                stringCacheStore = new LevelCacheStore();
                break;

            case "memory":
            default:
                // 默认使用ConcurrentHashMap作为缓存
                stringCacheStore = new InMemoryCacheStore();
                break;
        }
        log.info("mobiai cache store load impl: [{}]", stringCacheStore.getClass());
        return stringCacheStore;
    }


    /**
     * 初始化跨域Filter
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> corsFilter = new FilterRegistrationBean<>();

        corsFilter.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        corsFilter.setFilter(new CorsFilter());
        corsFilter.addUrlPatterns("/api/*");

        return corsFilter;
    }


    /**
     * 初始化日志过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<LogFilter> logFilter() {
        FilterRegistrationBean<LogFilter> logFilter = new FilterRegistrationBean<>();

        logFilter.setOrder(Ordered.HIGHEST_PRECEDENCE + 9);
        logFilter.setFilter(new LogFilter());
        logFilter.addUrlPatterns("/*");

        return logFilter;
    }

    @Bean
    public FilterRegistrationBean<ContentFilter> contentFilter(MobiaiProperties mobiaiProperties,
                                                               OptionService optionService,
                                                               StringCacheStore cacheStore,
                                                               OneTimeTokenService oneTimeTokenService) {
        ContentFilter contentFilter = new ContentFilter(mobiaiProperties, optionService, cacheStore, oneTimeTokenService);
        contentFilter.setFailureHandler(new ContentAuthenticationFailureHandler());

        String adminPattern = MobiaiUtils.ensureBoth(mobiaiProperties.getAdminPath(), "/") + "**";

        contentFilter.addExcludeUrlPatterns(
                adminPattern,
                "/api/**",
                "/install",
                "/version",
                "/js/**",
                "/css/**");

        FilterRegistrationBean<ContentFilter> contentFilterFilterRegistrationBean = new FilterRegistrationBean<>();
        contentFilterFilterRegistrationBean.setOrder(-1);
        contentFilterFilterRegistrationBean.setFilter(contentFilter);
        contentFilterFilterRegistrationBean.addUrlPatterns("/*");

        return contentFilterFilterRegistrationBean;
    }


    /**
     * 初始化调用api安全验证过滤器
     *
     * @param mobiaiProperties
     * @param objectMapper
     * @param optionService
     * @param cacheStore
     * @param oneTimeTokenService
     * @return
     */
    @Bean
    public FilterRegistrationBean<ApiAuthenticationFilter> apiAuthenticationFilter(MobiaiProperties mobiaiProperties,
                                                                                   ObjectMapper objectMapper,
                                                                                   OptionService optionService,
                                                                                   StringCacheStore cacheStore,
                                                                                   OneTimeTokenService oneTimeTokenService) {
        ApiAuthenticationFilter apiFilter = new ApiAuthenticationFilter(mobiaiProperties, optionService, cacheStore, oneTimeTokenService);
        apiFilter.addExcludeUrlPatterns(
                "/api/content/*/comments",
                "/api/content/**/comments/**",
                "/api/content/options/comment"
        );

        DefaultAuthenticationFailureHandler failureHandler = new DefaultAuthenticationFailureHandler();
        failureHandler.setProductionEnv(mobiaiProperties.isProductionEnv());
        failureHandler.setObjectMapper(objectMapper);

        // 设置故障处理程序
        apiFilter.setFailureHandler(failureHandler);

        FilterRegistrationBean<ApiAuthenticationFilter> authenticationFilter = new FilterRegistrationBean<>();
        authenticationFilter.setFilter(apiFilter);
        authenticationFilter.setOrder(0);
        authenticationFilter.addUrlPatterns("/api/content/*");

        return authenticationFilter;
    }

    /**
     * 用户安全管理过滤器
     *
     * @param cacheStore
     * @param userService
     * @param mobiaiProperties
     * @param objectMapper
     * @param optionService
     * @param oneTimeTokenService
     * @return
     */
    public FilterRegistrationBean<AdminAuthenticationFilter> adminAuthenticationFilter(StringCacheStore cacheStore,
                                                                                       UserService userService,
                                                                                       MobiaiProperties mobiaiProperties,
                                                                                       ObjectMapper objectMapper,
                                                                                       OptionService optionService,
                                                                                       OneTimeTokenService oneTimeTokenService) {
        AdminAuthenticationFilter adminAuthenticationFilter = new AdminAuthenticationFilter(cacheStore,
                userService,
                mobiaiProperties,
                optionService,
                oneTimeTokenService);

        DefaultAuthenticationFailureHandler failureHandler = new DefaultAuthenticationFailureHandler();
        failureHandler.setObjectMapper(objectMapper);
        failureHandler.setProductionEnv(mobiaiProperties.isProductionEnv());

        adminAuthenticationFilter.addExcludeUrlPatterns(
                "/api/admin/login",
                "/api/admin/refresh/*",
                "/api/admin/installations",
                "/api/admin/recoveries/migrations/*",
                "/api/admin/migrations/*",
                "/api/admin/is_installed",
                "/api/admin/password/code",
                "/api/admin/password/reset"
        );
        adminAuthenticationFilter.setFailureHandler(failureHandler);

        FilterRegistrationBean<AdminAuthenticationFilter> authenticationFilter = new FilterRegistrationBean<>();
        authenticationFilter.setFilter(adminAuthenticationFilter);
        authenticationFilter.addUrlPatterns("/api/admin/*", "/api/content/comments");
        authenticationFilter.setOrder(1);

        return authenticationFilter;
    }
}
