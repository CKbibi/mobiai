package com.cex0.mobiai.security.handler;

import com.cex0.mobiai.exception.MobiaiException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 身份验证失败处理程序。
 *
 * @author Cex0
 * date 2020/02/28
 */
public interface AuthenticationFailureHandler {


    /**
     * 当用户身份验证失败时调用。
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     * @throws ServletException
     */
    void onFailure(HttpServletRequest request, HttpServletResponse response, MobiaiException exception) throws IOException, ServletException;
}
