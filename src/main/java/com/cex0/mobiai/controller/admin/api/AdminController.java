package com.cex0.mobiai.controller.admin.api;

import com.cex0.mobiai.cache.lock.CacheLock;
import com.cex0.mobiai.model.annotation.DisableOnCondition;
import com.cex0.mobiai.model.params.LoginParam;
import com.cex0.mobiai.model.params.ResetPasswordParam;
import com.cex0.mobiai.model.properties.PrimaryProperties;
import com.cex0.mobiai.security.token.AuthToken;
import com.cex0.mobiai.service.AdminService;
import com.cex0.mobiai.service.OptionService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Cex0
 * @date 2020/03/05
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    private final OptionService optionService;

    public AdminController(AdminService adminService, OptionService optionService) {
        this.adminService = adminService;
        this.optionService = optionService;
    }

    /**
     * 项目是否初始化
     * @return
     */
    @GetMapping(value = "/is_installed")
    @ApiOperation("Check install status")
    public boolean isInstall() {
        return optionService.getByPropertyOrDefault(PrimaryProperties.IS_INSTALLED, Boolean.class, false);
    }


    /**
     * 登录接口
     * @param loginParam
     * @return
     */
    @PostMapping(value = "login")
    @ApiOperation("Login")
    @CacheLock(autoDelete = false)
    public AuthToken auth(@RequestBody @Valid LoginParam loginParam) {
        return adminService.authenticate(loginParam);
    }


    /**
     * 退出，清除token
     */
    @PostMapping("logout")
    @ApiOperation("Logs out (Clear Session)")
    @CacheLock(autoDelete = false)
    public void logout() {
        adminService.clearToken();
    }


    /**
     * 发送验证码找回密码
     *
     * @param param
     */
    @PostMapping("password/code")
    @ApiOperation("Sends reset password verify code")
    public void sendResetCode(@RequestBody @Valid ResetPasswordParam param) {
        adminService.sendResetPasswordCode(param);
    }


    /**
     * 验证验证码重制密码
     *
     * @param param
     */
    @PostMapping("password/reset")
    @DisableOnCondition
    @ApiOperation("Resets password by verify code")
    public void resetPassword(@RequestBody @Valid ResetPasswordParam param) {
        adminService.resetPasswordByCode(param);
    }


    @PostMapping("refresh/{refreshToken}")
    @ApiOperation("Refreshes token")
    @CacheLock(autoDelete = false)
    public AuthToken refresh(@PathVariable("refreshToken") String refreshToken) {
        return adminService.refreshToken(refreshToken);
    }


}
