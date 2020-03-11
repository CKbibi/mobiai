package com.cex0.mobiai.model.params;

import com.cex0.mobiai.model.enums.OptionType;
import lombok.Data;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/11 21:44
 * @Description: 查找option的参数
 */
@Data
public class OptionQuery {

    private String keyword;

    private OptionType type;
}
