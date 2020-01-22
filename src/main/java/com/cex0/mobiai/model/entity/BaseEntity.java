package com.cex0.mobiai.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@ToString
@MappedSuperclass
@EqualsAndHashCode
public class BaseEntity {


    /**
     * 创建时间
     */
    @Column(name = "create_time", columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time", columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    /**
     * 删除标识
     */
    @Column(name = "deleted", columnDefinition = "TINYINT default 0")
    private boolean delete;

    protected void prePersist() {
        delete = false;
        Date now = new Date();
        if (createTime == null) {
            createTime = now;
        }
        if (updateTime == null) {
            updateTime = now;
        }
    }

    protected void preUpdate() {
        updateTime = new Date();
    }

    protected void preRemove() {
        updateTime = new Date();
    }
}
