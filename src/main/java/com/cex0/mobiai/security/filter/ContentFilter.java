package com.cex0.mobiai.security.filter;

import com.cex0.mobiai.config.properties.MobiaiProperties;
import com.cex0.mobiai.service.OptionService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: wodenvyoujiaoshaxiong
 * @Date: 2020/3/1 16:37
 * @Description: Content Filter
 */
public class ContentFilter extends AbstractAuthenticationFilter{

    protected ContentFilter(MobiaiProperties mobiaiProperties, OptionService optionService) {
        super(mobiaiProperties, optionService);
    }

    @Override
    protected String getTokenFromRequest(HttpServletRequest request) {
        return null;
    }

    @Override
    protected void doAuthenticate(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(request, response);
    }
}
