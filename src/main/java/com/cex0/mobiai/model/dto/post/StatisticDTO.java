package com.cex0.mobiai.model.dto.post;

import lombok.Data;
import lombok.ToString;

/**
 * 统计DTO
 * @author Cex0
 * @date   2020年1月22日
 */
@Data
@ToString
public class StatisticDTO {

    private long postCount;

    private long commentCount;

    private long attachmentCount;

    private long birthday;

    private long establishDays;

    private long linkCount;

    private long visitCount;

    private long likeCount;
}
