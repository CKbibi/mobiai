package com.cex0.mobiai.service.impl;

import com.cex0.mobiai.model.dto.post.OptionDTO;
import com.cex0.mobiai.model.entity.Option;
import com.cex0.mobiai.model.enums.ValueEnum;
import com.cex0.mobiai.model.params.OptionParam;
import com.cex0.mobiai.model.properties.PropertyEnum;
import com.cex0.mobiai.service.OptionService;
import com.qiniu.common.Zone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class OptionServiceImpl implements OptionService {

    @Override
    public void save(Map<String, Object> options) {

    }

    @Override
    public void save(List<OptionParam> optionParams) {

    }

    @Override
    public void saveProperty(PropertyEnum property, String value) {

    }

    @Override
    public void saveProperties(Map<? extends PropertyEnum, String> properties) {

    }

    @Override
    public Map<String, Object> listOptions() {
        return null;
    }

    @Override
    public Map<String, Object> listOptions(List<String> keys) {
        return null;
    }

    @Override
    public List<OptionDTO> listDtos() {
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
    public long getBirthday() {
        return 0;
    }

    @Override
    public List<Option> listAll() {
        return null;
    }

    @Override
    public List<Option> listAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Option> listAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Option> listAllByIds(Collection<Integer> integers) {
        return null;
    }

    @Override
    public List<Option> listAllByIds(Collection<Integer> integers, Sort sort) {
        return null;
    }

    @Override
    public Optional<Option> fetchById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Option getById(Integer integer) {
        return null;
    }

    @Override
    public Option getByIdOfNullable(Integer integer) {
        return null;
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public void mustExistById(Integer integer) {

    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public Option create(Option option) {
        return null;
    }

    @Override
    public List<Option> createInBatch(Collection<Option> options) {
        return null;
    }

    @Override
    public Option update(Option option) {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public List<Option> updateInBatch(Collection<Option> options) {
        return null;
    }

    @Override
    public Option removeById(Integer integer) {
        return null;
    }

    @Override
    public Option removeByIdOfNullable(Integer integer) {
        return null;
    }

    @Override
    public void remove(Option option) {

    }

    @Override
    public void removeInBatch(Collection<Integer> integers) {

    }

    @Override
    public void removeAll(Collection<Option> options) {

    }

    @Override
    public void removeAll() {

    }
}
