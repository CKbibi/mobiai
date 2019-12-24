package com.cex0.mobiai.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;

/**
 *  用于对象工具类
 *
 * @author Cex0
 */
public class BeanUtils {

    private BeanUtils() {

    }


    /**
     *  从源对象转换。(只复制相同的属性)
     * @param source            源数据
     * @param targetClass       targetClass不能为空
     * @param <T>               targetClass的数据类型
     * @return                  从源数据复制指定类型的实例; 如果源数据为空，则为空
     * @exception Exception     如果新目标实例失败或复制失败
     */
    @Nullable
    public static <T> T transformFrom(@Nullable Object source, @NonNull Class<T> targetClass) {
        Assert.notNull(targetClass, "TargetClass must not be null!");

        if (ValidUtil.isEmpty(source)) {
            return null;
        }

        try {
            // newInstance出对象
            T targetInstance = targetClass.newInstance();
            // 复制
            org.springframework.beans.BeanUtils.copyProperties(source, targetInstance, getNullPropertyNames(source));
            // 返回
            return targetInstance;
        } catch (Exception e) {
            // TODO
            return null;
        }
    }


    /**
     * 从源对象转换。(只复制相同的属性) 批量操作
     * @param sources           源数据集合
     * @param targetClass       targetClass不能为空
     * @param <T>               targetClass的数据类型
     * @return                  从源数据复制指定类型的实例; 如果源数据为空，则为空
     * @exception Exception     如果新目标实例失败或复制失败
     */
    @NonNull
    public static <T> List<T> transformFromInBatch(Collection<?> sources, @NonNull Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sources)) {
            return Collections.emptyList();
        }
        // 批量操作
        return sources.stream().map(source -> transformFrom(source, targetClass)).collect(Collectors.toList());
    }


    /**
     * 更新属性(非空)。
     *
     * @param source            source不能为空
     * @param target            target不能为空
     * @throws Exception        复制的时候出现的错误
     */
    public static void updateProperties(@NonNull Object source, @NonNull Object target) {
        Assert.notNull(source, "source object must not be null");
        Assert.notNull(target, "target object must not be null");

        // 从源属性设置非空属性到目标属性
        try {
            org.springframework.beans.BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
        } catch (BeansException e) {
            // TODO
        }
    }


    /**
     * 获取属性的空名称数组。
     *
     * @param source    对象数据不能为空
     * @return          属性的空名称数组
     */
    private static String[] getNullPropertyNames(@NonNull Object source) {
        return getNullPropertyNameSet(source).toArray(new String[0]);
    }


    /**
     * 获取属性的空名称集合。
     *
     * @param source    对象数据不能为空
     * @return          属性的空名称集合
     */
    private static Set<String> getNullPropertyNameSet(@NonNull Object source) {
        Assert.notNull(source, "source object must be not null");
        Set<String> emptySet = new HashSet<>();

        // 获取对象属性
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String propertyName = propertyDescriptor.getName();

            // 如果属性的值为空的话，添加到集合中
            if (ValidUtil.isEmptyOrNull(beanWrapper.getPropertyValue(propertyName))) {
                emptySet.add(propertyName);
            }
        }

        return emptySet;
    }


}
