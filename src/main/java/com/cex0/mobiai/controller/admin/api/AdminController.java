package com.cex0.mobiai.controller.admin.api;

import com.cex0.mobiai.model.params.LoginParam;
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
    public AuthToken auth(@RequestBody @Valid LoginParam loginParam) {
        return adminService.authenticate(loginParam);
    }




}
