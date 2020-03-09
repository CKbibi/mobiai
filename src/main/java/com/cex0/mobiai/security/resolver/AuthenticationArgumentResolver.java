package com.cex0.mobiai.security.resolver;

import com.cex0.mobiai.exception.AuthenticationException;
import com.cex0.mobiai.model.entity.User;
import com.cex0.mobiai.security.authentication.Authentication;
import com.cex0.mobiai.security.context.SecurityContextHolder;
import com.cex0.mobiai.security.support.UserDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

/**
 * 身份验证参数解析程序。
 *
 * @author wodenvyoujiaoshaxiong
 * @date 2020/03/10
 */
@Slf4j
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

    public AuthenticationArgumentResolver() {
        log.debug("Initializing AuthenticationArgumentResolver");
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> parameterType = methodParameter.getParameterType();
        return (Authentication.class.isAssignableFrom(parameterType) ||
                UserDetail.class.isAssignableFrom(parameterType) ||
                User.class.isAssignableFrom(parameterType));
    }

    @Override
    @Nullable
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        log.debug("Handle AuthenticationArgument");
        Class<?> parameterType = methodParameter.getParameterType();

        Authentication authentication = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .orElseThrow(() -> new AuthenticationException("You haven't signed in yet"));

        if (Authentication.class.isAssignableFrom(parameterType)) {
            return authentication;
        }
        else if (UserDetail.class.isAssignableFrom(parameterType)) {
            return authentication.getDetail();
        }
        else if (User.class.isAssignableFrom(parameterType)) {
            return authentication.getDetail().getUser();
        }

        // 都没有抛出异常
        throw new UnsupportedOperationException("Unknown parameter type: " + parameterType);
    }
}
