package com.cex0.mobiai.repository.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class BaseRepositoryImpl<DOMAIN, ID> extends SimpleJpaRepository<DOMAIN, ID> implements BaseRepository<DOMAIN, ID> {

    private final JpaEntityInformation<DOMAIN, ID> entityInformation;

    private final EntityManager entityManager;

    public BaseRepositoryImpl(JpaEntityInformation<DOMAIN, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.entityManager = entityManager;
    }


    /**
     * 执行count查询并透明地汇总返回的所有值。
     * @param query
     * @return
     */
    private static long executeCountQuery(TypedQuery<Long> query) {
        Assert.notNull(query, "TypedQuery must not be null");

        List<Long> totals = query.getResultList();
        long total = 0L;

        for (Long element : totals) {
            total += element == null ? 0 : element;
        }

        return total;
    }


    /**
     * 通过ids查找所有对象接口
     * @param ids       对象主键id的集合，不能为空
     * @param sort      指定的排序
     * @return          返回对象集合
     */
    @Override
    public List<DOMAIN> findAllByIdIn(@NonNull Iterable<ID> ids, @NonNull Sort sort) {
        Assert.notNull(ids, "The given Iterable of Id's must not be null!");
        Assert.notNull(sort, "Sort into must not be null");

        log.debug("The given Iterable of Id's must not be null!");

        if (!ids.iterator().hasNext()) {
            return Collections.emptyList();
        }

        if (entityInformation.hasCompositeId()) {
            List<DOMAIN> results = new ArrayList<>();
            ids.forEach(id -> super.findById(id).ifPresent(results::add));
            return results;
        }

        ByIdsSpecification<DOMAIN> specification = new ByIdsSpecification<>(entityInformation);
        TypedQuery<DOMAIN> query = super.getQuery(specification, sort);
        return query.setParameter(specification.parameter, ids).getResultList();
    }

    @Override
    public Page<DOMAIN> findAllByIdIn(Iterable<ID> ids, Pageable pageable) {
        Assert.notNull(ids, "The given Iterable of Id's must not be null!");
        Assert.notNull(pageable, "Sort into must not be null");

        if (!ids.iterator().hasNext()) {
            return new PageImpl<>(Collections.emptyList());
        }

        if (entityInformation.hasCompositeId()) {
            throw new UnsupportedOperationException("Unsupported find all by composite id with page info");
        }

        ByIdsSpecification<DOMAIN> specification = new ByIdsSpecification<>(entityInformation);
        TypedQuery<DOMAIN> query = super.getQuery(specification, pageable).setParameter(specification.parameter, ids);
        TypedQuery<Long> countQuery = getCountQuery(specification, getDomainClass()).setParameter(specification.parameter, ids);

        return pageable.isUnpaged() ?
                new PageImpl<>(query.getResultList()) : readPage(query, getDomainClass(), pageable, countQuery);

    }

    @Override
    public long deleteByIdIn(Iterable<ID> ids) {
        log.debug("Customized deleteByIdIn method was invoked");
        List<DOMAIN> domains = findAllById(ids);

        deleteInBatch(domains);

        return domains.size();
    }


    protected <S extends DOMAIN> Page<S> readPage(TypedQuery<S> query, Class<S> domainClass, Pageable pageable, TypedQuery<Long> countQuery) {
        if (pageable.isPaged()) {
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }

        return PageableExecutionUtils.getPage(query.getResultList(), pageable, () -> executeCountQuery(countQuery));
    }


    private static final class ByIdsSpecification<T> implements Specification<T> {
        private static final long serialVersionUID = 1L;

        private final JpaEntityInformation<T, ?> entityInformation;
        @Nullable
        ParameterExpression<Iterable> parameter;

        ByIdsSpecification(JpaEntityInformation<T, ?> entityInformation) {
            this.entityInformation = entityInformation;
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            Path<?> path = root.get(this.entityInformation.getIdAttribute());
            this.parameter = criteriaBuilder.parameter(Iterable.class);
            return path.in(this.parameter);
        }
    }
}
