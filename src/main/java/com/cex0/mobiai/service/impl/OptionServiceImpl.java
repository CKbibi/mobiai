package com.cex0.mobiai.service.impl;

import com.cex0.mobiai.cache.StringCacheStore;
import com.cex0.mobiai.config.properties.MobiaiProperties;
import com.cex0.mobiai.event.options.OptionUpdatedEvent;
import com.cex0.mobiai.model.dto.post.OptionDTO;
import com.cex0.mobiai.model.dto.post.OptionSimpleDTO;
import com.cex0.mobiai.model.entity.Option;
import com.cex0.mobiai.model.enums.PostPermalinkType;
import com.cex0.mobiai.model.enums.ValueEnum;
import com.cex0.mobiai.model.params.OptionParam;
import com.cex0.mobiai.model.params.OptionQuery;
import com.cex0.mobiai.model.properties.PropertyEnum;
import com.cex0.mobiai.repository.OptionRepository;
import com.cex0.mobiai.service.OptionService;
import com.cex0.mobiai.service.base.AbstractCrudService;
import com.cex0.mobiai.util.ServiceUtils;
import com.cex0.mobiai.util.ValidationUtils;
import com.qiniu.common.Zone;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * OptionService implementation class
 *
 * @author Cex0
 * @date 2020/03/11
 */
@Slf4j
@Service
public class OptionServiceImpl extends AbstractCrudService<Option, Integer> implements OptionService {


    private final ApplicationContext applicationContext;
    private final StringCacheStore cacheStore;
    private final ApplicationEventPublisher eventPublisher;
    private final Map<String, PropertyEnum> propertyEnumMap;
    private final MobiaiProperties mobiaiProperties;
    private final OptionRepository optionRepository;

    public OptionServiceImpl(MobiaiProperties mobiaiProperties,
                             OptionRepository optionRepository,
                             ApplicationContext applicationContext,
                             StringCacheStore cacheStore,
                             ApplicationEventPublisher eventPublisher) {
        super(optionRepository);
        this.mobiaiProperties = mobiaiProperties;
        this.optionRepository = optionRepository;
        this.applicationContext = applicationContext;
        this.cacheStore = cacheStore;
        this.eventPublisher = eventPublisher;

        propertyEnumMap = Collections.unmodifiableMap(PropertyEnum.getValuePropertyEnumMap());
    }

    @Override
    public void save(Map<String, Object> optionMap) {
        if (CollectionUtils.isEmpty(optionMap)) {
            return;
        }

        Map<String, Option> optionKeyMap = ServiceUtils.convertToMap(listAll(), Option::getKey);

        List<Option> optionsToCreate = new LinkedList<>();
        List<Option> optionsToUpdate = new LinkedList<>();

        optionMap.forEach((key, value) -> {
            Option oldOption = optionKeyMap.get(key);
            if (oldOption == null || !StringUtils.equals(oldOption.getValue(), value.toString())) {
                OptionParam optionParam = new OptionParam();
                optionParam.setKey(key);
                optionParam.setValue(value.toString());
                ValidationUtils.validate(optionParam);

                if (oldOption == null) {
                    // Create it
                    optionsToCreate.add(optionParam.convertTo());
                } else if (!StringUtils.equals(oldOption.getValue(), value.toString())) {
                    // Update it
                    optionParam.update(oldOption);
                    optionsToUpdate.add(oldOption);
                }
            }
        });

        // Update them
        updateInBatch(optionsToUpdate);

        // Create them
        createInBatch(optionsToCreate);

        if (!CollectionUtils.isEmpty(optionsToUpdate) || !CollectionUtils.isEmpty(optionsToCreate)) {
            // If there is something changed
            publishOptionUpdatedEvent();
        }
    }

    @Override
    public void save(List<OptionParam> optionParams) {
        if (CollectionUtils.isEmpty(optionParams)) {
            return;
        }

        Map<String, Object> optionMap = ServiceUtils.convertToMap(optionParams, OptionParam::getKey, OptionParam::getValue);
        save(optionMap);
    }

    @Override
    public void save(OptionParam optionParam) {

    }

    @Override
    public void update(Integer optionId, OptionParam optionParam) {

    }

    @Override
    public void saveProperty(PropertyEnum property, String value) {

    }

    @Override
    public void saveProperties(Map<? extends PropertyEnum, String> properties) {

    }

    @Override
    public Map<String, Object> listOptions() {
        // 从缓存中获取options
        return cacheStore.getAny(OPTIONS_KEY, Map.class).orElseGet(() ->{
            List<Option> options = listAll();

            Set<String> keys = ServiceUtils.fetchProperty(options, Option::getKey);

            Map<String, Object> userDefinedOptionMap = ServiceUtils.convertToMap(options, Option::getKey, option -> {
                String key = option.getKey();

                PropertyEnum propertyEnum = propertyEnumMap.get(key);

                if (propertyEnum == null) {
                    return option.getValue();
                }

                return PropertyEnum.converTo(option.getValue(), propertyEnum);
            });

            Map<String, Object> result = new HashMap<>(userDefinedOptionMap);

            // 添加默认值
            propertyEnumMap.keySet()
                    .stream()
                    .filter(key -> !keys.contains(key))
                    .forEach(key -> {
                        PropertyEnum propertyEnum = propertyEnumMap.get(key);

                        if (StringUtils.isBlank(propertyEnum.defaultValue())) {
                            return;
                        }

                        result.put(key, PropertyEnum.converTo(propertyEnum.defaultValue(), propertyEnum));
                    });

            // 添加到缓存
            cacheStore.putAny(OPTIONS_KEY, result);
            return result;
        });
    }

    @Override
    public Map<String, Object> listOptions(List<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyMap();
        }

        Map<String, Object> optionMap = listOptions();

        Map<String, Object> result = new HashMap<>(keys.size());

        keys.stream()
                .filter(optionMap::containsKey)
                .forEach(key -> result.put(key, optionMap.get(key)));

        return result;
    }

    @Override
    public List<OptionDTO> listDtos() {
        List<OptionDTO> result = new LinkedList<>();

        listOptions().forEach((key, value) -> result.add(new OptionDTO(key, value)));

        return result;
    }

    @Override
    public Page<OptionSimpleDTO> pageDtosBy(Pageable pageable, OptionQuery optionQuery) {
        return null;
    }

    @Override
    public Option removePermanently(Integer id) {
        return null;
    }

    @Override
    public Object getByKeyOfNullable(String key) {
        return null;
    }

    @Override
    public Object getByKeyOfNonNull(String key) {
        return null;
    }

    @Override
    public Optional<Object> getByKey(String key) {
        return Optional.empty();
    }

    @Override
    public Object getByPropertyOfNullable(PropertyEnum property) {
        return null;
    }

    @Override
    public Object getByPropertyOfNonNull(PropertyEnum property) {
        return null;
    }

    @Override
    public Optional<Object> getByProperty(PropertyEnum property) {
        return Optional.empty();
    }

    @Override
    public <T> T getByPropertyOrDefault(PropertyEnum property, Class<T> propertyType, T defaultValue) {
        return null;
    }

    @Override
    public <T> T getByPropertyOrDefault(PropertyEnum property, Class<T> propertyType) {
        return null;
    }

    @Override
    public <T> Optional<T> getByProperty(PropertyEnum property, Class<T> propertyType) {
        return Optional.empty();
    }

    @Override
    public <T> T getByKeyOrDefault(String key, Class<T> valueType, T defaultValue) {
        return null;
    }

    @Override
    public <T> Optional<T> getByKey(String key, Class<T> valueType) {
        return Optional.empty();
    }

    @Override
    public <T extends Enum<T>> Optional<T> getEnumByProperty(PropertyEnum property, Class<T> valueType) {
        return Optional.empty();
    }

    @Override
    public <T extends Enum<T>> T getEnumByPropertyOrDefault(PropertyEnum property, Class<T> valueType, T defaultValue) {
        return null;
    }

    @Override
    public <V, E extends ValueEnum<V>> Optional<E> getValueEnumByProperty(PropertyEnum property, Class<V> valueType, Class<E> enumType) {
        return Optional.empty();
    }

    @Override
    public <V, E extends ValueEnum<V>> E getValueEnumByPropertyOrDefault(PropertyEnum property, Class<V> valueType, Class<E> enumType, E defaultValue) {
        return null;
    }

    @Override
    public int getPostPageSize() {
        return 0;
    }

    @Override
    public int getArchivesPageSize() {
        return 0;
    }

    @Override
    public int getCommentPageSize() {
        return 0;
    }

    @Override
    public int getRssPageSize() {
        return 0;
    }

    @Override
    public Zone getQnYunZone() {
        return null;
    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public String getBlogBaseUrl() {
        return null;
    }

    @Override
    public String getBlogTitle() {
        return null;
    }

    @Override
    public String getSeoKeywords() {
        return null;
    }

    @Override
    public String getSeoDescription() {
        return null;
    }

    @Override
    public long getBirthday() {
        return 0;
    }

    @Override
    public PostPermalinkType getPostPermalinkType() {
        return null;
    }

    @Override
    public String getSheetPrefix() {
        return null;
    }

    @Override
    public String getLinksPrefix() {
        return null;
    }

    @Override
    public String getPhotosPrefix() {
        return null;
    }

    @Override
    public String getJournalsPrefix() {
        return null;
    }

    @Override
    public String getArchivesPrefix() {
        return null;
    }

    @Override
    public String getCategoriesPrefix() {
        return null;
    }

    @Override
    public String getTagsPrefix() {
        return null;
    }

    @Override
    public String getPathSuffix() {
        return null;
    }

    @Override
    public Boolean isEnabledAbsolutePath() {
        return null;
    }

    @Override
    public List<OptionDTO> replaceUrl(String oldUrl, String newUrl) {
        return null;
    }

    @Override
    public OptionSimpleDTO convertToDto(Option option) {
        return null;
    }

    private void cleanCache() {
        cacheStore.delete(OPTIONS_KEY);
    }

    private void publishOptionUpdatedEvent() {
        flush();
        cleanCache();
        eventPublisher.publishEvent(new OptionUpdatedEvent(this));
    }
}
