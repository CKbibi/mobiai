package com.cex0.mobiai.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import com.cex0.mobiai.exception.NotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * CrudService接口包含一些常用的方法。
 * @param <DOMAIN>  实体类型
 * @param <ID>      id类型
 */
public interface CrudService<DOMAIN, ID> {

    /**
     * 查找所有
     *
     * @return List
     */
    @NonNull
    List<DOMAIN> listAll();

    /**
     * 查找所有排序
     *
     * @param sort sort
     * @return List
     */
    @NonNull
    List<DOMAIN> listAll(@NonNull Sort sort);

    /**
     * 分页查找所有
     *
     * @param pageable pageable
     * @return Page
     */
    @NonNull
    Page<DOMAIN> listAll(@NonNull Pageable pageable);

    /**
     * 通过id查找
     *
     * @param ids ids
     * @return List
     */
    @NonNull
    List<DOMAIN> listAllByIds(@Nullable Collection<ID> ids);

    /**
     * 查找所有通过id并排序
     *
     * @param ids  ids
     * @param sort sort
     * @return List
     */
    @NonNull
    List<DOMAIN> listAllByIds(@Nullable Collection<ID> ids, @NonNull Sort sort);

    /**
     * 通过id获取单个
     *
     * @param id id
     * @return Optional
     */
    @NonNull
    Optional<DOMAIN> fetchById(@NonNull ID id);

    /**
     * 通过id获取对象
     *
     * @param id id
     * @return DOMAIN
     * @throws NotFoundException If the specified id does not exist
     */
    @NonNull
    DOMAIN getById(@NonNull ID id);

    /**
     * 可以通过id为空的值获取
     *
     * @param id id
     * @return DOMAIN
     */
    @Nullable
    DOMAIN getByIdOfNullable(@NonNull ID id);

    /**
     * id是否存在
     *
     * @param id id
     * @return boolean
     */
    boolean existsById(@NonNull ID id);

    /**
     * 必须按id存在，否则抛出NotFoundException。
     *
     * @param id id
     * @throws NotFoundException If the specified id does not exist
     */
    void mustExistById(@NonNull ID id);

    /**
     * 所有的数量
     *
     * @return long
     */
    long count();

    /**
     * 保存
     *
     * @param domain domain
     * @return DOMAIN
     */
    @NonNull
    @Transactional
    DOMAIN create(@NonNull DOMAIN domain);

    /**
     * 批量保存
     *
     * @param domains domains
     * @return List
     */
    @NonNull
    @Transactional
    List<DOMAIN> createInBatch(@NonNull Collection<DOMAIN> domains);

    /**
     * 更新实体
     *
     * @param domain domain
     * @return DOMAIN
     */
    @NonNull
    @Transactional
    DOMAIN update(@NonNull DOMAIN domain);

    /**
     * 清除数据库中所有挂起的更改。
     */
    void flush();

    /**
     * 批量修改
     *
     * @param domains domains
     * @return List
     */
    @NonNull
    @Transactional
    List<DOMAIN> updateInBatch(@NonNull Collection<DOMAIN> domains);

    /**
     * 通过id移除
     *
     * @param id id
     * @return DOMAIN
     * @throws NotFoundException If the specified id does not exist
     */
    @NonNull
    @Transactional
    DOMAIN removeById(@NonNull ID id);

    /**
     * 如果存在，则通过id删除。
     *
     * @param id id
     * @return DOMAIN
     */
    @Nullable
    @Transactional
    DOMAIN removeByIdOfNullable(@NonNull ID id);

    /**
     * 通过实体对象删除
     *
     * @param domain domain
     */
    @Transactional
    void remove(@NonNull DOMAIN domain);

    /**
     * 通过id集合删除
     *
     * @param ids ids
     */
    @Transactional
    void removeInBatch(@NonNull Collection<ID> ids);

    /**
     * 通过实体对象删除
     *
     * @param domains domains
     */
    @Transactional
    void removeAll(@NonNull Collection<DOMAIN> domains);

    /**
     * 删除所有
     */
    @Transactional
    void removeAll();
}
