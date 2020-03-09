package com.cex0.mobiai.config.properties;

import com.cex0.mobiai.model.enums.Mode;
import com.cex0.mobiai.model.support.MobiaiConst;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

import static com.cex0.mobiai.model.support.MobiaiConst.*;
import static com.cex0.mobiai.util.MobiaiUtils.ensureSuffix;

/**
 * Mobiai 配置类
 *
 * @author Cex0
 */
@Data
@ConfigurationProperties("mobiai")
public class MobiaiProperties {

    /**
     * 文档api已禁用。（默认为true）
     */
    private boolean docDisabled = true;

    /**
     * 生产环境。（默认为true）
     */
    private boolean productionEnv = true;

    /**
     * 已启用身份验证
     */
    private boolean authEnabled = true;

    /**
     * 启动模式。
     */
    private Mode mode = Mode.PRODUCTION;

    /**
     * 用户路径
     */
    private String adminPath = "admin";

    /**
     * 工作目录。
     */
    private String workDir = ensureSuffix(USER_HOME, FILE_SEPARATOR) + ".mobiai" + FILE_SEPARATOR;

    /**
     * 备份目录。（不建议修改此配置）；
     */
    private String backupDir = ensureSuffix(TEMP_DIR, FILE_SEPARATOR) + "mobiai-backup" + FILE_SEPARATOR;

    /**
     * 上传前缀。
     */
    private String uploadUrlPrefix = "upload";

    /**
     * 下载超时。
     */
    private Duration downloadTimeout = Duration.ofSeconds(30);

    /**
     * cache 的实现接口 1：memory 2：level
     */
    private String cache = "memory";

}
