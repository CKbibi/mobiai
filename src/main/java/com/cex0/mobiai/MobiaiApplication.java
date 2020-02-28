package com.cex0.mobiai;

import com.cex0.mobiai.repository.base.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableAsync
@EnableJpaRepositories(basePackages = "com.cex0.mobiai.repository", repositoryBaseClass = BaseRepositoryImpl.class)
public class MobiaiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        System.setProperty("spring.config.additional-location", "file:${user.home}/.mobiai/,file:${user.home}/mobiai-dev/");

        SpringApplication.run(MobiaiApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        System.setProperty("spring.config.additional-location", "file:${user.home}/.mobiai/,file:${user.home}/mobiai-dev/");
        return application.sources(MobiaiApplication.class);
    }

}
