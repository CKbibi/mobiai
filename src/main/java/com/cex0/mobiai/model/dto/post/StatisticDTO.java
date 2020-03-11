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

    private Long postCount;

    private Long commentCount;

    private Long categoryCount;

    @Deprecated
    private Long attachmentCount;

    private Long tagCount;

    private Long journalCount;

    private Long birthday;

    private Long establishDays;

    private Long linkCount;

    private Long visitCount;

    private Long likeCount;
}
