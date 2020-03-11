package com.cex0.mobiai.model.dto.post;

import com.cex0.mobiai.model.dto.base.OutputConverter;
import com.cex0.mobiai.model.entity.Option;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * option DTO
 * @author wodenvyoujiaoshaxiong
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionDTO implements OutputConverter<OptionDTO, Option> {

    private String key;

    private String value;
}
