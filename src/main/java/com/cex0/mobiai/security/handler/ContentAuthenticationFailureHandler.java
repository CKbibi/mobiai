package com.cex0.mobiai.security.handler;

import com.cex0.mobiai.exception.MobiaiException;
import com.cex0.mobiai.exception.NotInstallException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/1 21:00
 * @Description: 内容认证失败处理程序
 */
public class ContentAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onFailure(HttpServletRequest request, HttpServletResponse response, MobiaiException exception) throws IOException, ServletException {
        if (exception instanceof NotInstallException) {
            response.sendRedirect(request.getContextPath() + "/install");
            return;
        }

        request.getRequestDispatcher(request.getContextPath() + "/error").forward(request, response);
    }
}
