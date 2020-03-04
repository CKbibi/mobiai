package com.cex0.mobiai.security.handler;

import cn.hutool.extra.servlet.ServletUtil;
import com.cex0.mobiai.exception.MobiaiException;
import com.cex0.mobiai.model.BaseResponse;
import com.cex0.mobiai.util.ExceptionUtils;
import com.cex0.mobiai.util.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: wodenvyoujiaoshaxiong
 * @Date: 2020/2/28 21:11
 * @Description:
 */
@Slf4j
public class DefaultAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private boolean productionEnv = true;

    private ObjectMapper objectMapper = JsonUtils.DEFAULT_JSON_MAPPER;

    public DefaultAuthenticationFailureHandler() {

    }

    @Override
    public void onFailure(HttpServletRequest request, HttpServletResponse response, MobiaiException exception) throws IOException, ServletException {
        log.warn("Handle unsuccessful anthentication, ip: [{}]", ServletUtil.getClientIP(request));
        log.error("Authentication failure", exception);

        BaseResponse<Object> errorDetail = new BaseResponse<>();
        errorDetail.setStatus(exception.getStatus().value());
        errorDetail.setDevMessage(exception.getMessage());
        errorDetail.setData(exception.getErrorData());

        if (!productionEnv) {
            errorDetail.setDevMessage(ExceptionUtils.getStackTrace(exception));
        }

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(exception.getStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(errorDetail));
    }


    /**
     * 设置定制的ObjectMapping
     *
     * @param objectMapper
     */
    public void setObjectMapper(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "ObjectMapper must not be null");

        this.objectMapper = objectMapper;
    }


    /**
     * 设置是否生产环境
     *
     * @param productionEnv
     */
    public void setProductionEnv(boolean productionEnv) {
        this.productionEnv = productionEnv;
    }

}
