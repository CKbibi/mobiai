package com.cex0.mobiai.model.dto.post;

import com.cex0.mobiai.model.enums.Mode;
import lombok.Data;

/**
 * 主题控制器
 * @author Cex0
 * @date   2020年1月22日
 */
@Data
public class EnvironmentDTO {

    private String database;

    private Long startTime;

    private String version;

    private Mode mode;
}
