package com.cex0.mobiai.event.user;

import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * 用户更新事件。（创建或更新）
 *
 * @author Cex0
 * @date 2020/03/05
 */
public class UserUpdatedEvent extends ApplicationEvent {

    private final Integer userId;


    /**
     * 用户跟新事件
     *
     * @param source
     * @param userId
     */
    public UserUpdatedEvent(Object source, @NonNull Integer userId) {
        super(source);

        Assert.notNull(userId, "User id must not be null");

        this.userId = userId;
    }

    @NonNull
    public Integer getUserId() {
        return userId;
    }
}
