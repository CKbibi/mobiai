package com.cex0.mobiai.service;

import com.cex0.mobiai.model.dto.post.OptionDTO;
import com.cex0.mobiai.model.dto.post.OptionSimpleDTO;
import com.cex0.mobiai.model.entity.Option;
import com.cex0.mobiai.model.enums.PostPermalinkType;
import com.cex0.mobiai.model.enums.ValueEnum;
import com.cex0.mobiai.model.params.OptionParam;
import com.cex0.mobiai.model.params.OptionQuery;
import com.cex0.mobiai.model.properties.PropertyEnum;
import com.cex0.mobiai.service.base.CrudService;
import com.qiniu.common.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import com.cex0.mobiai.exception.MissingPropertyException;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Option service interface
 *
 * @author Cex0
 * @date 2020/02/28
 */
public interface OptionService extends CrudService<Option, Integer> {

    int DEFAULT_POST_PAGE_SIZE = 10;

    int DEFAULT_COMMENT_PAGE_SIZE = 10;

    int DEFAULT_ARCHIVES_PAGE_SIZE = 10;

    int DEFAULT_RSS_PAGE_SIZE = 20;

    String OPTIONS_KEY = "options";

    /**
     * 保存多个设置
     *
     * @param options options
     */
    @Transactional
    void save(@Nullable Map<String, Object> options);

    /**
     * 保存多个设置
     *
     * @param optionParams option params
     */
    @Transactional
    void save(@Nullable List<OptionParam> optionParams);

    /**
     * 保存单个操作
     *
     * @param optionParam
     */
    void save(@Nullable OptionParam optionParam);

    /**
     * 通过id修改option
     *
     * @param optionId
     * @param optionParam
     */
    void update(@NonNull Integer optionId, @NonNull OptionParam optionParam);

    /**
     * 保存
     *
     * @param property must not be null
     * @param value    could be null
     */
    @Transactional
    void saveProperty(@NonNull PropertyEnum property, @Nullable String value);

    /**
     * 保存博客属性。
     *
     * @param properties blog properties
     */
    @Transactional
    void saveProperties(@NonNull Map<? extends PropertyEnum, String> properties);

    /**
     * 获取所有选项
     *
     * @return Map
     */
    @NonNull
    @Transactional
    Map<String, Object> listOptions();

    /**
     * 获取选项by key list.
     *
     * @param keys key list
     * @return a map of option
     */
    @NonNull
    Map<String, Object> listOptions(@Nullable List<String> keys);

    /**
     * 获取所有选项dtos.
     *
     * @return a list of option dto
     */
    @NonNull
    List<OptionDTO> listDtos();

    /**
     * 分页输出DTO
     * @param pageable
     * @param optionQuery
     * @return
     */
    Page<OptionSimpleDTO> pageDtosBy(@NonNull Pageable pageable, OptionQuery optionQuery);

    /**
     * 永久删除选项。
     *
     * @param id option id must not be null
     * @return option detail deleted
     */
    @NonNull
    Option removePermanently(@NonNull Integer id);

    /**
     * 获取选项by key
     *
     * @param key option key must not be blank
     * @return option value or null
     */
    @Nullable
    Object getByKeyOfNullable(@NonNull String key);

    /**
     * Gets option value of non null.
     * 获取非空的选项值。
     *
     * @param key option key must not be null
     * @return option value of non null
     */
    @NonNull
    Object getByKeyOfNonNull(@NonNull String key);

    /**
     * 获取选项by key
     *
     * @param key option key must not be blank
     * @return an optional option value
     */
    @NonNull
    Optional<Object> getByKey(@NonNull String key);

    /**
     * 获取选项by blog property.
     *
     * @param property blog property must not be null
     * @return an option value
     */
    @Nullable
    Object getByPropertyOfNullable(@NonNull PropertyEnum property);

    /**
     * 获取选项值by blog property.
     *
     * @param property blog property
     * @return an optiona value
     * @throws MissingPropertyException throws when property value dismisses
     */
    @NonNull
    Object getByPropertyOfNonNull(@NonNull PropertyEnum property);

    /**
     * Gets option value by blog property.
     * 获取选项值by blog property.
     *
     * @param property blog property must not be null
     * @return an optional option value
     */
    @NonNull
    Optional<Object> getByProperty(@NonNull PropertyEnum property);

    /**
     * Gets property value by blog property.
     * 获取选项值by blog property.
     *
     * @param property     blog property must not be null
     * @param propertyType property type must not be null
     * @param defaultValue default value
     * @param <T>          property type
     * @return property value
     */
    <T> T getByPropertyOrDefault(@NonNull PropertyEnum property, @NonNull Class<T> propertyType, T defaultValue);

    /**
     * Gets property value by blog property.
     * 获取选项值by blog property.
     *
     * @param property     blog property must not be null
     * @param propertyType property type must not be null
     * @param <T>          property type
     * @return property value
     */
    <T> T getByPropertyOrDefault(@NonNull PropertyEnum property, @NonNull Class<T> propertyType);


    /**
     * Gets property value by blog property.
     * 获取选项值by blog property.
     *
     * @param property     blog property must not be null
     * @param propertyType property type must not be null
     * @param <T>          property type
     * @return property value
     */
    <T> Optional<T> getByProperty(@NonNull PropertyEnum property, @NonNull Class<T> propertyType);

    /**
     * Gets value by key.
     * 获取值by key.
     *
     * @param key          key must not be null
     * @param valueType    value type must not be null
     * @param defaultValue default value
     * @param <T>          property type
     * @return value
     */
    <T> T getByKeyOrDefault(@NonNull String key, @NonNull Class<T> valueType, T defaultValue);

    /**
     * Gets value by key.
     * 获取值by key.
     *
     * @param key       key must not be null
     * @param valueType value type must not be null
     * @param <T>       value type
     * @return value
     */
    @NonNull
    <T> Optional<T> getByKey(@NonNull String key, @NonNull Class<T> valueType);

    /**
     * Gets enum value by property.
     * 获取枚举by property.
     *
     * @param property  property must not be blank
     * @param valueType enum value type must not be null
     * @param <T>       enum value type
     * @return an optional enum value
     */
    @NonNull
    <T extends Enum<T>> Optional<T> getEnumByProperty(@NonNull PropertyEnum property, @NonNull Class<T> valueType);

    /**
     * Gets enum value by property.
     * 获取枚举by property.
     *
     * @param property     property must not be blank
     * @param valueType    enum value type must not be null
     * @param defaultValue default value
     * @param <T>          enum value type
     * @return enum value
     */
    @Nullable
    <T extends Enum<T>> T getEnumByPropertyOrDefault(@NonNull PropertyEnum property, @NonNull Class<T> valueType, @Nullable T defaultValue);

    /**
     * Gets value enum by property.
     * 获取枚举by property.
     *
     * @param property  property must not be blank
     * @param valueType enum value type must not be null
     * @param enumType  enum type must not be null
     * @param <V>       enum value type
     * @param <E>       value enum type
     * @return an optional value enum value
     */
    @NonNull
    <V, E extends ValueEnum<V>> Optional<E> getValueEnumByProperty(@NonNull PropertyEnum property, @NonNull Class<V> valueType, @NonNull Class<E> enumType);

    /**
     * Gets value enum by property.
     * 获取枚举by property.
     *
     * @param property     property must not be blank
     * @param valueType    enum value type must not be null
     * @param enumType     enum type must not be null
     * @param defaultValue default value enum value
     * @param <V>          enum value type
     * @param <E>          value enum type
     * @return value enum value or null if the default value is null
     */
    @Nullable
    <V, E extends ValueEnum<V>> E getValueEnumByPropertyOrDefault(@NonNull PropertyEnum property, @NonNull Class<V> valueType, @NonNull Class<E> enumType, @Nullable E defaultValue);


    /**
     * Gets post page size.
     * 获取页数
     *
     * @return page size
     */
    int getPostPageSize();

    /**
     * 获取存档页大小。
     *
     * @return page size
     */
    int getArchivesPageSize();

    /**
     * Gets comment page size.
     * 获取评论页数
     *
     * @return page size
     */
    int getCommentPageSize();

    /**
     * Gets rss page size.
     * 获取页数
     *
     * @return page size
     */
    int getRssPageSize();

    /**
     * Get qi niu yun zone.
     * 获取七牛
     *
     * @return qiniu zone
     */
    @NonNull
    Zone getQnYunZone();

    /**
     * Gets locale.
     *
     * @return locale user set or default locale
     */
    @NonNull
    Locale getLocale();

    /**
     * Gets blog base url. (Without /)
     *
     * @return blog base url (If blog url isn't present, current machine IP address will be default)
     */
    @NonNull
    String getBlogBaseUrl();

    /**
     * Gets blog title.
     *
     * @return blog title.
     */
    @NonNull
    String getBlogTitle();

    /**
     * 获取全局搜索引擎优化关键字。
     *
     * @return keywords
     */
    String getSeoKeywords();

    /**
     * 获取全局搜索引擎优化描述。
     *
     * @return description
     */
    String getSeoDescription();

    /**
     * Gets blog birthday.
     *
     * @return birthday timestamp
     */
    long getBirthday();

    /**
     * 获取post permalink类型。
     *
     * @return PostPermalinkType
     */
    PostPermalinkType getPostPermalinkType();

    /**
     * 获取工作表自定义前缀。
     *
     * @return sheet prefix.
     */
    String getSheetPrefix();

    /**
     * 获取链接页自定义前缀。
     *
     * @return links page prefix.
     */
    String getLinksPrefix();

    /**
     * 获取照片页自定义前缀。
     *
     * @return photos page prefix.
     */
    String getPhotosPrefix();

    /**
     * 获取日志页自定义前缀。
     *
     * @return journals page prefix.
     */
    String getJournalsPrefix();

    /**
     * 获取存档自定义前缀。
     *
     * @return archives prefix.
     */
    String getArchivesPrefix();

    /**
     * 获取类别自定义前缀。
     *
     * @return categories prefix.
     */
    String getCategoriesPrefix();

    /**
     * 获取标记自定义前缀。
     *
     * @return tags prefix.
     */
    String getTagsPrefix();

    /**
     * 获取自定义路径后缀。
     *
     * @return path suffix.
     */
    String getPathSuffix();

    /**
     * 已启用绝对路径。
     *
     * @return true or false.
     */
    Boolean isEnabledAbsolutePath();

    /**
     * 批量替换选项url。
     *
     * @param oldUrl old blog url.
     * @param newUrl new blog url.
     * @return replaced options.
     */
    List<OptionDTO> replaceUrl(@NonNull String oldUrl, @NonNull String newUrl);

    /**
     * 转换为选项输出dto。
     *
     * @param option option must not be null
     * @return an option output dto
     */
    @NonNull
    OptionSimpleDTO convertToDto(@NonNull Option option);
}
