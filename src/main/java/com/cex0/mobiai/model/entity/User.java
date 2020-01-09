package com.cex0.mobiai.model.entity;

import com.cex0.mobiai.util.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * 用户实体
 * @author Cex0
 * @date: 2020-01-09
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity{

    private Integer id;

    private String username;

    private String nickname;

    private String password;

    private String email;

    private String avatar;

    private String description;

    private Date expireTime;

    @Override
    public void prePersist() {
        super.prePersist();

        id = null;

        if (email == null) {
            email = "";
        }

        if (avatar == null) {
            avatar = "";
        }

        if (description == null) {
            description = "";
        }

        if (expireTime == null) {
            expireTime = DateUtils.now();
        }
    }
}
