package com.cex0.mobiai.util;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 反射工具类
 */
public class ReflectionUtils {

    private ReflectionUtils() {}


    /**
     * 获取参数化类型
     * @param superType     超类类型 不能为空（类或接口）
     * @param genericTypes  通用类型数组
     * @return
     */
    @Nullable
    public static ParameterizedType getParameterizedType(@NonNull Class<?> superType, Type ... genericTypes) {
        Assert.notNull(superType, "Interface or super type must not be null");

        ParameterizedType currentType = null;

        for (Type genericType : genericTypes) {
            if (genericType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericType;
                if (parameterizedType.getRawType().getTypeName().equals(superType.getTypeName())) {
                    currentType = parameterizedType;
                    break;
                }
            }
        }

        return currentType;
    }


    /**
     * 获取参数化类型
     *
     * @param interfaceType       接口类型  不能为空
     * @param implementationClass 接口实现类或者接口 不能为空
     * @return parameterized type of the interface or null if it is mismatch
     */
    @Nullable
    public static ParameterizedType getParameterizedType(@NonNull Class<?> interfaceType, Class<?> implementationClass) {
        Assert.notNull(interfaceType, "Interface type must not be null");
        Assert.isTrue(interfaceType.isInterface(), "The give type must be an interface");

        if (implementationClass == null) {
            // If the super class is Object parent then return null
            return null;
        }

        // Get parameterized type
        ParameterizedType currentType = getParameterizedType(interfaceType, implementationClass.getGenericInterfaces());

        if (currentType != null) {
            // return the current type
            return currentType;
        }

        Class<?> superclass = implementationClass.getSuperclass();

        return getParameterizedType(interfaceType, superclass);
    }


    /**
     * 通过超类获取参数化类型
     *
     * @param superClassType 超类 不能为空
     * @param extensionClass 拓展类
     * @return parameterized type or null
     */
    @Nullable
    public static ParameterizedType getParameterizedTypeBySuperClass(@NonNull Class<?> superClassType, Class<?> extensionClass) {

        if (extensionClass == null) {
            return null;
        }

        return getParameterizedType(superClassType, extensionClass.getGenericSuperclass());
    }
}
