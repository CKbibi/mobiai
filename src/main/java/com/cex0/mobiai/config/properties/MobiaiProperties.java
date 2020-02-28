package com.cex0.mobiai.config.properties;

import com.cex0.mobiai.model.support.MobiaiConst;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Mobiai 配置类
 *
 * @author Cex0
 */
@Data
@ToString
@ConfigurationProperties("mobiai")
public class MobiaiProperties {

    /**
     * Doc api disabled. (Default is true)
     */
    private boolean docDisabled = true;

    /**
     * Production env. (Default is true)
     */
    private boolean productionEnv = true;

    /**
     * Authentication enabled
     */
    private boolean authEnabled = true;

    /**
     * Work directory.
     */
    private String workDir = MobiaiConst.USER_HOME + "/.mobiai/";

    public MobiaiProperties() throws IOException {
        // Create work directory if not exist
        Files.createDirectories(Paths.get(workDir));
    }
}
