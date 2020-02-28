package com.cex0.mobiai.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * 设置实体
 *
 * @author Cex0
 */
@Data
@ToString
@Entity
@Table(name = "options")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Option extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "option_key", columnDefinition = "varchar(100) not null")
    private String key;

    @Column(name = "option_value", columnDefinition = "varchar(1023) not null")
    private String value;

    public Option(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    protected void prePersist() {
        super.prePersist();
        id = null;
    }
}
