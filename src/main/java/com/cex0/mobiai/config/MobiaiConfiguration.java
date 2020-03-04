package com.cex0.mobiai.config;

import com.cex0.mobiai.cache.InMemoryCacheStore;
import com.cex0.mobiai.cache.StringCacheStore;
import com.cex0.mobiai.config.properties.MobiaiProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mobiai configuration.
 * @author wodenvyoujiaoshaxiong
 */
@Configuration
@EnableConfigurationProperties(MobiaiProperties.class)
@Slf4j
public class MobiaiConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public StringCacheStore stringCacheStore() {
        return new InMemoryCacheStore();
    }
}
