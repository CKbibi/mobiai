package com.cex0.mobiai.service;

import com.cex0.mobiai.model.dto.post.EnvironmentDTO;
import com.cex0.mobiai.model.dto.post.StatisticDTO;
import com.cex0.mobiai.model.params.LoginParam;
import com.cex0.mobiai.model.params.ResetPasswordParam;
import com.cex0.mobiai.security.token.AuthToken;
import org.springframework.lang.NonNull;

public interface AdminService {

    /**
     * 过期时间（秒)
     */
    int ACCESS_TOKEN_EXPIRED_SECONDS = 24 * 3600;

    int REFRESH_TOKEN_EXPIRED_DAYS = 30;

    String APPLICATION_CONFIG_NAME = "application.yaml";

    String LOGS_PATH = "logs/spring.log";


    /**
     * 进行身份验证
     * @param loginParam 登录信息不能为空
     * @return      身份验证信息token
     */
    @NonNull
    AuthToken authenticate(@NonNull LoginParam loginParam);


    /**
     * 清除token
     */
    void clearToken();


    /**
     * 发送重置密码代码到管理员的电子邮件。
     * @param param  不能为空
     */
    void sendResetPasswordCode(@NonNull ResetPasswordParam param);


    /**
     * 通过code重制密码
     * @param param  不能为空
     */
    void resetPasswordByCode(@NonNull ResetPasswordParam param);


    /**
     * 获取系统次数
     * @return 获取DTO次数
     */
    StatisticDTO getCount();


    /**
     * 获取运行环境
     * @return  运行环境
     */
    EnvironmentDTO getEnvironments();


    /**
     * 刷新token
     * @param refreshToken token，不能为空
     * @return             信息验证token
     */
    @NonNull
    AuthToken refreshToken(@NonNull String refreshToken);


    /**
     * 更新用户管理
     */
    void updateAdminAssets();


    /**
     * 获取Spring日志
     * @return
     */
    String getSpringLogs();
}
