package com.cex0.mobiai.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;

/**
 *  Json Utils
 * @author Cex0
 */
public class JsonUtils {

    /**
     * default json mapper.
     */
    public final static ObjectMapper DEFAULT_JSON_MAPPER = createDefaultJsonMapper();

    /**
     * create default json mapper
     * @return object mapper
     */
    public static ObjectMapper createDefaultJsonMapper() {
        return createDefaultJsonMapper(null);
    }


    /**
     * create default json mapper
     * @param strategy      命名规则
     * @return              ObjectMapper对象
     */
    public static ObjectMapper createDefaultJsonMapper(@Nullable PropertyNamingStrategy strategy) {
        ObjectMapper mapper = new ObjectMapper();
        // 配置
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        if (strategy != null) {
            mapper.setPropertyNamingStrategy(strategy);
        }
        return mapper;
    }


    /**
     * 将json转换为指定的对象类型。
     * @param json              json字符串，不能为空
     * @param type              指定转换的类型，不能为空
     * @param <T>               返回的类型与传入的类型相同
     * @return                  转换后的对象类型
     * @throws IOException      转换失败时抛出
     */
    public static <T> T jsonToObject(@NonNull String json, @NonNull Class<T> type) throws IOException {
        return jsonToObject(json, type, DEFAULT_JSON_MAPPER);
    }


    /**
     * 将json转换为指定的对象类型。
     * @param json              json字符串，不能为空或者空字符串
     * @param type              指定转换的类型， 不能为空
     * @param objectMapper      objectMapper，不能为空
     * @param <T>               返回的类型与传入的类型相同
     * @return                  转换后的对象类型
     * @throws IOException      转换失败时抛出
     */
    public static <T> T jsonToObject(@NonNull String json,
                                     @NonNull Class<T> type,
                                     @Nullable ObjectMapper objectMapper) throws IOException {
        Assert.hasText(json, "Json content must not be blank");
        Assert.notNull(type, "Target type must not be null");
        Assert.notNull(objectMapper, "ObjectMapper must not be null");

        return objectMapper.readValue(json, type);
    }


    /**
     * 将指定类型对象转换为json。
     * @param source                        需要转换的对象，不能为空
     * @return                              返回json字符串
     * @throws JsonProcessingException      转换失败时抛出
     */
    public static String objectToJson(@Nullable Object source) throws JsonProcessingException {
        return objectToJson(source, DEFAULT_JSON_MAPPER);
    }


    /**
     * 将指定类型对象转换为json
     * @param source                        需要转换的对象，不能为空
     * @param objectMapper                  ObjectMapper对象，可以为空
     * @return                              返回json字符串
     * @throws JsonProcessingException      转换失败时抛出
     */
    public static String objectToJson(@NonNull Object source, @NonNull ObjectMapper objectMapper) throws JsonProcessingException {
        Assert.notNull(source, "Source object must not be null");
        Assert.notNull(objectMapper, "ObjectMapper must not be null");

        return objectMapper.writeValueAsString(source);
    }


    /**
     * 将map转换为指定类型对象
     * @param sourceMap         源对象，不能为空
     * @param type              转换的类型，不能问为空
     * @param <T>               转换的类型
     * @return                  返回执行类型对象
     * @throws IOException      转换失败时抛出
     */
    public static <T> T mapToObject(@NonNull Map<String, ?> sourceMap, @NonNull Class<T> type) throws IOException {
        return mapToObject(sourceMap, type, DEFAULT_JSON_MAPPER);
    }


    /**
     * 将map转换为指定类型对象
     * @param sourceMap         源对象，不能为空
     * @param type              转换的类型，不能问为空
     * @param objectMapper      ObjectMapper对象
     * @param <T>               转换的类型
     * @return                  返回执行类型对象
     * @throws IOException      转换失败时抛出
     */
    public static <T> T mapToObject(@NonNull Map<String, ?> sourceMap,
                                    @NonNull Class<T> type,
                                    @NonNull ObjectMapper objectMapper) throws IOException {
        Assert.notEmpty(sourceMap, "Source map must not null");

        // 序列化成json字符串
        String json = objectToJson(sourceMap, objectMapper);

        return jsonToObject(json, type, objectMapper);
    }


    /**
     * 将指定类型对象转换为map对象
     * @param source            源对象，不能为空
     * @return                  Map对象
     * @throws IOException      转换失败时抛出
     */
    public static Map<?, ?> objectToMap(@NonNull Object source) throws IOException {
        return objectToMap(source, DEFAULT_JSON_MAPPER);
    }


    /**
     * 将指定类型对象转换为map对象
     * @param source            源对象，不能为空
     * @param objectMapper      ObjectMapper对象
     * @return                  Map对象
     * @throws IOException      转换失败时抛出
     */
    public static Map<?, ?> objectToMap(@NonNull Object source, @NonNull ObjectMapper objectMapper) throws IOException {
        // 序列化源对象
        String json = objectToJson(source, objectMapper);

        // 反序列化json
        return jsonToObject(json, Map.class, objectMapper);
    }
}
