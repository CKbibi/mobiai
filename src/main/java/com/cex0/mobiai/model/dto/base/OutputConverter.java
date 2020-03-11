package com.cex0.mobiai.model.dto.base;

import org.springframework.lang.NonNull;
import static com.cex0.mobiai.util.BeanUtils.updateProperties;

/**
 * <b>实现类型必须等于DTO类型<b/>
 *
 * @param <DOMAIN> 实现类类型
 * @param <DTO> 实体类型
 *
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/11 21:32
 * @Description: 输出DTO的转换器接口。
 */
public interface OutputConverter<DTO extends OutputConverter<DTO, DOMAIN>, DOMAIN> {

    /**
     * 转换
     *
     * @param domain
     * @return
     */
    @SuppressWarnings("unchecked")
    @NonNull
    default <T extends DTO> T convertFrom(@NonNull DOMAIN domain) {

        updateProperties(domain, this);

        return (T) this;
    }
}
