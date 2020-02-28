package com.cex0.mobiai.config;

import com.cex0.mobiai.config.properties.MobiaiProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MobiaiProperties.class)
@Slf4j
public class MobiaiConfiguration {
}
