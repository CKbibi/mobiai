package com.cex0.mobiai.model.params;

import com.cex0.mobiai.model.dto.base.InputConverter;
import com.cex0.mobiai.model.entity.User;
import com.cex0.mobiai.model.support.CreateCheck;
import com.cex0.mobiai.model.support.UpdateCheck;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * @Auther: wodenvyoujiaoshaxiong
 * @Date: 2020/3/1 15:19
 * @Description:
 */
@Data
public class UserParam implements InputConverter<User> {

    @NotBlank(message = "用户名不能为空", groups = {CreateCheck.class, UpdateCheck.class})
    @Size(max = 50, message = "用户名字符长度不能超过 {max}", groups = {CreateCheck.class, UpdateCheck.class})
    private String username;

    @NotBlank(message = "用户昵称不能为空", groups = {CreateCheck.class, UpdateCheck.class})
    @Size(max = 255, message = "用户昵称字符长度不能超过 {max}", groups = {CreateCheck.class, UpdateCheck.class})
    private String nickname;

    @Email(message = "电子邮件地址的格式不正确", groups = {CreateCheck.class, UpdateCheck.class})
    @NotBlank(message = "电子邮箱地址不能为空", groups = {CreateCheck.class, UpdateCheck.class})
    @Size(max = 127, message = "电子邮箱地址字符长度不能超过 {max}", groups = {CreateCheck.class, UpdateCheck.class})
    private String email;

    @Null(groups = UpdateCheck.class)
    @Size(min = 8, max = 100, message = "密码的字符长度必须在 {min} - {max} 之间", groups = {CreateCheck.class})
    private String password;

    @Size(max = 1023, message = "头像链接地址的字符长度不能超过 {max}", groups = {CreateCheck.class, UpdateCheck.class})
    private String avatar;

    @Size(max = 1023, message = "用户描述的字符长度不能超过 {max}", groups = {CreateCheck.class, UpdateCheck.class})
    private String description;
}
