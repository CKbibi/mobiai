package com.cex0.mobiai.model.entity;

import com.cex0.mobiai.model.enums.LogType;
import com.cex0.mobiai.util.ServletUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

/**
 * 日志对象
 * @author Cex0
 * @date 2020/03/05
 */
@Data
@Entity
@Table(name = "logs")
@ToString
@EqualsAndHashCode
public class Log extends BaseEntity {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * log key
     */
    @Column(name = "log_key", columnDefinition = "varchar(1023) default ''")
    private String logKey;


    /**
     * log类型
     */
    @Column(name = "type", columnDefinition = "int not null")
    private LogType type;


    /**
     * log内容
     */
    @Column(name = "content", columnDefinition = "varchar(1023) not null")
    private String content;


    /**
     * ip地址
     */
    @Column(name = "ip_address", columnDefinition = "varchar(127) default ''")
    private String ipAddress;


    @Override
    protected void prePersist() {
        super.prePersist();

        id = null;

        if (logKey == null) {
            logKey = "";
        }

        ipAddress = ServletUtils.getRequestIp();

        if (ipAddress == null) {
            logKey = "";
        }
    }
}
