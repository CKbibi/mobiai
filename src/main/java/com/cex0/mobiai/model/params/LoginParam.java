package com.cex0.mobiai.model.params;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 登录信息
 */
@Data
@ToString
public class LoginParam {

    @Size(max = 255, message = "用户名或者邮箱不能超过{max}")
    @NotBlank(message = "用户名或者邮箱不能为空")
    private String username;

    @Size(max = 100, message = "用户密码不能超过{max}")
    @NotBlank(message = "用户密码不能为空")
    private String password;
}
