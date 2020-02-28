package com.cex0.mobiai.model.dto.base;

import com.cex0.mobiai.util.BeanUtils;
import com.cex0.mobiai.util.ReflectionUtils;
import org.springframework.lang.Nullable;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

/**
 * 用于输入DTO的转换器接口。
 *
 * @author  cex0
 */
public interface InputConverter<DOMAIN> {


    /**
     * 转换成domain
     * @return  新的实体类（不为空）
     */
    @SuppressWarnings("unchecked")
    default DOMAIN convertTo() {
        ParameterizedType currentType = parameterizedType();

        Objects.requireNonNull(currentType, "Cannot fetch actual type because parameterized type is null");

        Class<DOMAIN> domainClass = (Class<DOMAIN>) currentType.getActualTypeArguments()[0];

        return BeanUtils.transformFrom(this, domainClass);
    }

    /**
     * 跟新实体
     *
     * @param domain updated domain
     */
    default void update(DOMAIN domain) {
        BeanUtils.updateProperties(this, domain);
    }


    /**
     * 获取类型
     *
     * @return parameterized type or null
     */
    @Nullable
    default ParameterizedType parameterizedType() {
        return ReflectionUtils.getParameterizedType(InputConverter.class, this.getClass());
    }
}
