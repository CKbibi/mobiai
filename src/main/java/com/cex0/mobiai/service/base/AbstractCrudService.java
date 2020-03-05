package com.cex0.mobiai.service.base;

import com.cex0.mobiai.repository.base.BaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.cex0.mobiai.exception.NotFoundException;

/**
 * 抽象curd实现
 *
 * @param <DOMAIN>  domain type
 * @param <ID>      id type
 */
@Slf4j
public class AbstractCrudService<DOMAIN, ID> implements CrudService<DOMAIN, ID> {

    private final String domainName;

    private final BaseRepository<DOMAIN, ID> repository;

    protected AbstractCrudService(BaseRepository<DOMAIN, ID> repository) {
        this.repository = repository;

        Class<DOMAIN> domainClass = (Class<DOMAIN>) fetchType(0);
        domainName = domainClass.getSimpleName();
    }


    /**
     * 获取实际的泛型类型。
     *
     * @param index  泛型类型的索引
     * @return
     */
    private Type fetchType(int index) {
        Assert.isTrue(index >= 0 && index <= 1, "Type index must be between 0 to 1");

        return ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[index];
    }


    /**
     * 获取所有
     *
     * @return
     */
    @Override
    public List<DOMAIN> listAll() {
        return repository.findAll();
    }


    /**
     * 获取所有按排序
     *
     * @param sort sort
     * @return
     */
    @Override
    public List<DOMAIN> listAll(Sort sort) {
        Assert.notNull(sort, "Sort info must not be null");
        return repository.findAll(sort);
    }


    /**
     * 按照分页查询
     *
     * @param pageable pageable
     * @return
     */
    @Override
    public Page<DOMAIN> listAll(Pageable pageable) {
        Assert.notNull(pageable, "Pageable info must not be null");

        return repository.findAll(pageable);
    }


    /**
     * 通过id集合获取对象集合
     *
     * @param ids ids
     * @return
     */
    @Override
    public List<DOMAIN> listAllByIds(Collection<ID> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : repository.findAllById(ids);
    }


    /**
     * 通过id集合获取对象集合并排序
     *
     * @param ids ids
     * @return
     */
    @Override
    public List<DOMAIN> listAllByIds(Collection<ID> ids, Sort sort) {
        Assert.notNull(sort, "Sort info must not be null");

        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : repository.findAllByIdIn(ids, sort);
    }


    /**
     * 按id获取
     *
     * @param id id
     * @return
     */
    @Override
    public Optional<DOMAIN> fetchById(ID id) {
        Assert.notNull(id, domainName + " id must not be null");

        return repository.findById(id);
    }


    /**
     * 通过id获取，如没找到抛出异常
     *
     * @param id id
     * @return
     * @throws NotFoundException 如果指定的id不存在
     */
    @Override
    public DOMAIN getById(ID id) {
        return fetchById(id).orElseThrow(() -> new NotFoundException(domainName + " was not found or has been deleted"));
    }


    /**
     * 通过id获取对象可为空
     *
     * @param id id
     * @return
     */
    @Override
    public DOMAIN getByIdOfNullable(ID id) {
        return fetchById(id).orElse(null);
    }


    /**
     * 通过id查看是否存在
     *
     * @param id id
     * @return
     */
    @Override
    public boolean existsById(ID id) {
        Assert.notNull(id, domainName + "id must not be null");

        return repository.existsById(id);
    }


    /**
     * 通过id查看是否存在，不存在抛出异常
     *
     * @param id id
     * @throws NotFoundException 如果查找的不存在
     */
    @Override
    public void mustExistById(ID id) {
        if (existsById(id)) {
            throw new NotFoundException(domainName + " was not exist");
        }
    }


    /**
     * 获取频次
     *
     * @return
     */
    @Override
    public long count() {
        return repository.count();
    }


    /**
     * 保存
     *
     * @param domain domain
     * @return
     */
    @Override
    public DOMAIN create(DOMAIN domain) {
        Assert.notNull(domain, domainName + " data must not be null");

        return repository.save(domain);
    }


    /**
     * 批量保存
     *
     * @param domains domains
     * @return
     */
    @Override
    public List<DOMAIN> createInBatch(Collection<DOMAIN> domains) {
        return CollectionUtils.isEmpty(domains) ? Collections.emptyList() : repository.saveAll(domains);
    }


    /**
     * 更新
     *
     * @param domain domain
     * @return
     */
    @Override
    public DOMAIN update(DOMAIN domain) {
        Assert.notNull(domain, domainName + " data must not be null");

        return repository.saveAndFlush(domain);
    }


    /**
     * 清空
     */
    @Override
    public void flush() {
        repository.flush();
    }


    /**
     * 批量更新
     *
     * @param domains domains
     * @return
     */
    @Override
    public List<DOMAIN> updateInBatch(Collection<DOMAIN> domains) {
        return CollectionUtils.isEmpty(domains) ? Collections.emptyList() : repository.saveAll(domains);
    }


    /**
     * 通过id移除
     *
     * @param id id
     * @return
     */
    @Override
    public DOMAIN removeById(ID id) {
        DOMAIN domain = getById(id);

        remove(domain);

        return domain;
    }


    /**
     * 如果存在按id删除
     *
     * @param id id
     * @return
     */
    @Override
    public DOMAIN removeByIdOfNullable(ID id) {
        return fetchById(id).map(domain -> {
            remove(domain);
            return domain;
        }).orElse(null);
    }


    /**
     * 移除对象
     * @param domain domain
     */
    @Override
    public void remove(DOMAIN domain) {
        Assert.notNull(domain, domainName + " data must not be null");

        repository.delete(domain);
    }


    /**
     * 批量移除
     *
     * @param ids ids
     */
    @Override
    public void removeInBatch(Collection<ID> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            log.warn(domainName + " id collection is empty");
            return;
        }

        repository.deleteByIdIn(ids);
    }


    /**
     * 移除所有对象
     *
     * @param domains domains
     */
    @Override
    public void removeAll(Collection<DOMAIN> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            log.warn(domainName + " collection is empty");
            return;
        }
        repository.deleteInBatch(domains);
    }


    /**
     * 删除所有
     */
    @Override
    public void removeAll() {
        repository.deleteAll();
    }
}
