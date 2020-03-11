package com.cex0.mobiai.util;

import org.springframework.data.domain.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/11 23:04
 * @Description: service utils
 */
public class ServiceUtils {

    private ServiceUtils(){}


    /**
     * 获取ID
     *
     * @param datas             集合
     * @param mappingFunction   计算数据列表中的id
     * @param <ID>              ID类型
     * @param <T>               date类型
     * @return
     */
    @NonNull
    public static <ID, T> Set<ID> fetchProperty(final Collection<T> datas, Function<T, ID> mappingFunction) {
        return CollectionUtils.isEmpty(datas) ?
                Collections.emptySet() :
                datas.stream().map(mappingFunction).collect(Collectors.toSet());
    }

    /**
     * 将列表转换为列表映射，其中列表包含id中的id。
     *
     * @param ids
     * @param list
     * @param mappingFunction
     * @param <ID>
     * @param <D>
     * @return 一种映射，其中键在id中，值在list中
     */
    @NonNull
    public static <ID, D> Map<ID, List<D>> convertToListMap(Collection<ID> ids, Collection<D> list, Function<D, ID> mappingFunction) {
        Assert.notNull(mappingFunction, "mapping function must not be null");

        if (CollectionUtils.isEmpty(ids) || CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }

        Map<ID, List<D>> resultMap = new HashMap<>();

        list.forEach(data -> resultMap.computeIfAbsent(mappingFunction.apply(data), id -> new LinkedList<>()).add(data));

        ids.forEach(id -> resultMap.putIfAbsent(id, Collections.emptyList()));

        return resultMap;
    }

    /**
     * 转换为映射（来自列表数据的键）
     *
     * @param list
     * @param mappingFunction
     * @param <ID>
     * @param <D>
     * @return 列表数据和值中的键是数据的映射
     */
    @NonNull
    public static <ID, D> Map<ID, D> convertToMap(Collection<D> list, Function<D, ID> mappingFunction) {
        Assert.notNull(mappingFunction, "mapping function must not be null");

        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }

        Map<ID, D> resultMap = new HashMap<>();

        list.forEach(data -> resultMap.putIfAbsent(mappingFunction.apply(data), data));

        return resultMap;
    }

    /**
     * 转换为映射（来自列表数据的键）
     *
     * @param list
     * @param keyFunction
     * @param valueFunction
     * @param <ID>
     * @param <D>
     * @param <V>
     * @return 列表数据和值中的键是数据的映射
     */
    @NonNull
    public static <ID, D, V> Map<ID, V> convertToMap(@Nullable Collection<D> list,
                                                     @NonNull Function<D, ID> keyFunction,
                                                     @NonNull Function<D, V> valueFunction) {
        Assert.notNull(keyFunction, "Key function must not be null");
        Assert.notNull(valueFunction, "Value function must not be null");

        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }

        Map<ID, V> resultMap = new HashMap<>();

        list.forEach(data -> resultMap.putIfAbsent(keyFunction.apply(data), valueFunction.apply(data)));

        return resultMap;
    }

    /**
     * 检查给定的号码id是否为空id。
     * @param id
     * @return 如果给定的数字id为空id，则为true；否则为false
     */
    public static boolean isEmptyId(@Nullable Number id) {
        return id == null || id.longValue() <= 0;
    }

    /**
     * 生成最新的页面请求。
     *
     * @param top
     * @return 最新的页面请求。
     */
    public static Pageable buildLatestPageable(int top) {
        return buildLatestPageable(top, "createTime");
    }

    /**
     * 生成最新的页面请求
     *
     * @param top
     * @param sortProperty 按这个属性进行排序
     * @return
     */
    public static Pageable buildLatestPageable(int top, @NonNull String sortProperty) {
        Assert.isTrue(top > 0, "Top number must not be less than 0");
        Assert.hasText(sortProperty, "Sort property must not be blank");

        return PageRequest.of(0, top, Sort.by(Sort.Direction.DESC, sortProperty));
    }

    /**
     * 生成空页结果。
     *
     * @param page
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T, S> Page<T> buildEmptyPageImpl(@NonNull Page<S> page) {
        Assert.notNull(page, "Page result must not be null");

        return new PageImpl<>(Collections.emptyList(), page.getPageable(), page.getTotalElements());
    }
}
