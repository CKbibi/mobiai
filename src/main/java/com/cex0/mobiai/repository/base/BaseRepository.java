package com.cex0.mobiai.repository.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;
import java.util.List;

/**
 * 基本存储库接口包含一些常用方法。
 * @param <DOMAIN>  实体类型
 * @param <ID>      id类型
 * @author Cex0
 */
@NoRepositoryBean
public interface BaseRepository<DOMAIN, ID> extends JpaRepository<DOMAIN, ID> {


    /**
     * 通过ids查找所有对象接口
     * @param ids       对象主键id的集合，不能为空
     * @param sort      指定的排序
     * @return          返回对象集合
     */
    @NonNull
    List<DOMAIN> findAllByIdIn(@NonNull Iterable<ID> ids, @NonNull Sort sort);


    /**
     * 通过ids查找所有对象接口分页
     * @param ids           对象主键id的集合，不能为空
     * @param pageable      分页
     * @return              返回对象集合
     */
    @NonNull
    Page<DOMAIN> findAllByIdIn(@NonNull Iterable<ID> ids, @NonNull Pageable pageable);


    /**
     * 通过ids删除对象
     * @param ids       对象主键id的集合，不能为空
     * @return          删除的行数
     */
    @NonNull
    long deleteByIdIn(@NonNull Iterable<ID> ids);
}
