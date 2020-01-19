package com.cex0.mobiai;

import com.cex0.mobiai.repository.base.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableAsync
@EnableJpaRepositories(basePackages = "com.cex0.mobiai.repository", repositoryBaseClass = BaseRepositoryImpl.class)
public class MobiaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MobiaiApplication.class, args);
    }

}
