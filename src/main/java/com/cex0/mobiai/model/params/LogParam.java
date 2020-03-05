package com.cex0.mobiai.model.params;

import com.cex0.mobiai.model.dto.base.InputConverter;
import com.cex0.mobiai.model.entity.Log;
import com.cex0.mobiai.model.enums.LogType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Cex0
 * @date 2020/03/05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogParam implements InputConverter<Log> {

    @Size(max = 1023, message = "Length of log key must not be more than {max}")
    private String logKey;

    @NotNull(message = "Log type must not be null")
    private LogType type;

    @NotBlank(message = "Log content must not be blank")
    @Size(max = 1023, message = "Log content must not be more than 1023")
    private String content;
}
