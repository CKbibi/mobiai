package com.cex0.mobiai.model.params;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;


/**
 * 重制密码信息
 *
 * @author Cex0
 * @date   2020年1月22日
 */
@ToString
@Data
public class ResetPasswordParam {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "邮箱不能为空")
    private String email;

    private String code;

    private String password;
}
