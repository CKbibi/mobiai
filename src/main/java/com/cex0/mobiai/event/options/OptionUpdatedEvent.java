package com.cex0.mobiai.event.options;

import org.springframework.context.ApplicationEvent;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/11 22:22
 * @Description:
 */
public class OptionUpdatedEvent extends ApplicationEvent {

    public OptionUpdatedEvent(Object source) {
        super(source);
    }
}
