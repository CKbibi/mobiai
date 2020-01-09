package com.cex0.mobiai.security.support;

import lombok.*;
import org.apache.catalina.User;

/**
 * 用户详细信息
 * @author Cex0
 */
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDetail {

    private User user;


}
