package com.cex0.mobiai.repository;

import com.cex0.mobiai.model.entity.Option;
import com.cex0.mobiai.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/11 23:01
 * @Description:
 */
public interface OptionRepository extends BaseRepository<Option, Integer>, JpaSpecificationExecutor<Option> {

    /**
     * 通过key查找option
     *
     * @param key
     * @return
     */
    Optional<Option> findByKey(String key);

    /**
     * 通过key删除option
     *
     * @param key
     */
    void deleteByKey(String key);
}
