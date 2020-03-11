package com.cex0.mobiai.model.dto.post;

import com.cex0.mobiai.model.enums.OptionType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/11 21:39
 * @Description: Option List output dto。
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OptionSimpleDTO extends OptionDTO {

    private Integer id;

    private OptionType optionType;

    private Date createTime;

    private Date updateTime;
}
