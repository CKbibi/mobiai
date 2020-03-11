package com.cex0.mobiai.model.properties;


import com.cex0.mobiai.model.enums.ValueEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Property enum.
 *
 * @author Cex0
 */
public interface PropertyEnum extends ValueEnum<String> {


    /**
     * 转换类型
     *
     * @param value string value must not be blank
     * @param type  property value type must not be null
     * @param <T>    property value type
     * @return
     */
    @SuppressWarnings("unchecked")
    static <T> T convertTo(@NonNull String value, @NonNull Class<T> type) {
        Assert.notNull(value, "Value must not be null");
        Assert.notNull(type, "type must not be null");

        if (type.isAssignableFrom(String.class)) {
            return (T) value;
        }

        if (type.isAssignableFrom(Integer.class)) {
            return (T) value;
        }

        if (type.isAssignableFrom(Long.class)) {
            return (T) value;
        }

        if (type.isAssignableFrom(Boolean.class)) {
            return (T) value;
        }

        if (type.isAssignableFrom(Short.class)) {
            return (T) value;
        }

        if (type.isAssignableFrom(Byte.class)) {
            return (T) value;
        }

        if (type.isAssignableFrom(Double.class)) {
            return (T) value;
        }

        if (type.isAssignableFrom(Float.class)) {
            return (T) value;
        }

        throw new UnsupportedOperationException("Unsupported convention for blog property type:" + type.getName() + "provided");
    }


    /**
     * 转换为具有相应类型的值
     * @param value
     * @param propertyEnum
     * @return
     */
    @SuppressWarnings("unchecked")
    static Object converTo(@Nullable String value, @NonNull PropertyEnum propertyEnum) {
        Assert.notNull(propertyEnum, "Property enum must not be null");

        if (StringUtils.isBlank(value)) {
            value = propertyEnum.defaultValue();
        }

        try {
            if (propertyEnum.getType().isAssignableFrom(Enum.class)) {
                Class<Enum> type = (Class<Enum>) propertyEnum.getType();
                Enum result = convertToEnum(value, type);
                return result != null ? result : value;
            }
            return convertTo(value, propertyEnum.getType());
        }
        catch (Exception e) {
            return value;
        }
    }


    /**
     * 转换成枚举
     * @param value     值不能为空
     * @param type       这是枚举类型，返回一个常量的类的对象，不能为空
     * @param <T>       转换的enum类型
     * @return          转换后的枚举或者null
     */
    static <T extends Enum<T>> T convertToEnum(@NonNull String value, @NonNull Class<T> type) {
        Assert.hasText(value, "Property value must not be blank");

        try {
            return Enum.valueOf(type, value.toUpperCase());
        }
        catch (Exception e) {
            return null;
        }
    }


    /**
     * 检查属性是否支持该类型。
     *
     * @param type type to check
     * @return true if supports; false else
     */
    static boolean isSupportedType(Class<?> type) {
        return type != null && (
                type.isAssignableFrom(String.class)
                        || type.isAssignableFrom(Number.class)
                        || type.isAssignableFrom(Integer.class)
                        || type.isAssignableFrom(Long.class)
                        || type.isAssignableFrom(Boolean.class)
                        || type.isAssignableFrom(Short.class)
                        || type.isAssignableFrom(Byte.class)
                        || type.isAssignableFrom(Double.class)
                        || type.isAssignableFrom(Float.class)
                        || type.isAssignableFrom(Enum.class)
                        || type.isAssignableFrom(ValueEnum.class)
        );
    }


//    static Map<String, PropertyEnum> getValuePropertyEnumMap() {
//        // Get all properties
//        List<Class<? extends PropertyEnum>> propertyEnumClasses = new LinkedList<>();
//        propertyEnumClasses.add(AliOssProperties.class);
//        propertyEnumClasses.add(AttachmentProperties.class);
//        propertyEnumClasses.add(BlogProperties.class);
//        propertyEnumClasses.add(CommentProperties.class);
//        propertyEnumClasses.add(EmailProperties.class);
//        propertyEnumClasses.add(OtherProperties.class);
//        propertyEnumClasses.add(PostProperties.class);
//        propertyEnumClasses.add(SheetProperties.class);
//        propertyEnumClasses.add(PrimaryProperties.class);
//        propertyEnumClasses.add(QiniuOssProperties.class);
//        propertyEnumClasses.add(SeoProperties.class);
//        propertyEnumClasses.add(UpOssProperties.class);
//        propertyEnumClasses.add(ApiProperties.class);
//        propertyEnumClasses.add(StaticDeployProperties.class);
//        propertyEnumClasses.add(GitStaticDeployProperties.class);
//        propertyEnumClasses.add(NetlifyStaticDeployProperties.class);
//        propertyEnumClasses.add(PermalinkProperties.class);
//
//        Map<String, PropertyEnum> result = new HashMap<>();
//
//        propertyEnumClasses.forEach(propertyEnumClass -> {
//            PropertyEnum[] propertyEnums = propertyEnumClass.getEnumConstants();
//
//            for (PropertyEnum propertyEnum : propertyEnums) {
//                result.put(propertyEnum.getValue(), propertyEnum);
//            }
//        });
//
//        return result;
//    }


    /**
     * 获取类型
     *
     * @return property type
     */
    Class<?> getType();


    /**
     * 默认值
     *
     * @return default value
     */
    String defaultValue();


    /**
     * 通过类型获取到默认值
     *
     * @param propertyType
     * @param <T>
     * @return
     */
    default <T> T defaultValue(Class<T> propertyType) {
        // 获取默认值
        String defaultValue = defaultValue();
        if (defaultValue == null) {
            return null;
        }

        return PropertyEnum.convertTo(defaultValue, propertyType);
    }
}
