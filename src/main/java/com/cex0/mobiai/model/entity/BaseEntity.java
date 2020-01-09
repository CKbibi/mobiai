package com.cex0.mobiai.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@EqualsAndHashCode
public class BaseEntity {

    private Date createTime;

    private Date updateTime;

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
