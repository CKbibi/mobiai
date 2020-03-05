package com.cex0.mobiai.event.logger;

import com.cex0.mobiai.model.enums.LogType;
import com.cex0.mobiai.model.params.LogParam;
import com.cex0.mobiai.util.ValidationUtils;
import org.springframework.context.ApplicationEvent;

/**
 * @author Cex0
 * @date 2020/03/05
 */
public class LogEvent extends ApplicationEvent {

    private final LogParam logParam;

    public LogEvent(Object source, LogParam logParam) {
        super(source);

        ValidationUtils.validate(logParam);

        this.logParam = logParam;
    }

    public LogEvent(Object source, String logKey, LogType logType, String content) {
        this(source, new LogParam(logKey, logType, content));
    }

    public LogParam getLogParam() {
        return logParam;
    }
}
