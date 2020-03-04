package com.cex0.mobiai.listener;

import com.cex0.mobiai.config.properties.MobiaiProperties;
import com.cex0.mobiai.service.OptionService;
import com.cex0.mobiai.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @Auther: wodenvyoujiaoshaxiong
 * @Date: 2020/3/4 21:15
 * @Description: 这是项目起的后会执行的方法
 */
@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StartedListener implements ApplicationListener<ApplicationEvent> {

    @Autowired
    private MobiaiProperties mobiaiProperties;

    @Autowired
    private OptionService optionService;

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {

    }
}
