package com.cex0.mobiai.model.params;

import com.cex0.mobiai.model.dto.base.InputConverter;
import com.cex0.mobiai.model.entity.Option;
import com.cex0.mobiai.model.enums.OptionType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Optional param
 *
 * @author wodenvyoujiaoshaxiong
 * @date 2020
 */
@Data
public class OptionParam implements InputConverter<Option> {


    @NotBlank(message = "Option key must not be blank")
    @Size(max = 100, message = "Length of option key must not be more than {max}")
    private String key;

    @Size(max = 1023, message = "Length of option value must not be more than {max}")
    private String value;

    private OptionType type;
}
