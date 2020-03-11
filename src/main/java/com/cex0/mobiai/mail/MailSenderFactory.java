package com.cex0.mobiai.mail;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.lang.NonNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Properties;

/**
 * java mail sender factory
 *
 * @author Cex0
 */
public class MailSenderFactory {


    /**
     * 获取邮件发件人。
     *
     * @param mailProperties
     * @return
     */
    @NonNull
    public JavaMailSender getMailSender(@NonNull MailProperties mailProperties) {
        Assert.notNull(mailProperties, "Mail properties must not be null");

        // 创建一个邮件发送者
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // 设置properties
        setProperties(mailSender, mailProperties);

        return mailSender;
    }

    private void setProperties(@NonNull JavaMailSenderImpl mailSender, @NonNull MailProperties mailProperties) {
        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());
        mailSender.setProtocol(mailProperties.getProtocol());
        mailSender.setDefaultEncoding(mailProperties.getDefaultEncoding().name());

        if (!CollectionUtils.isEmpty(mailProperties.getProperties())) {
            Properties properties = new Properties();
            properties.putAll(mailProperties.getProperties());
            mailSender.setJavaMailProperties(properties);
        }
    }
}
