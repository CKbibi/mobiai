package com.cex0.mobiai.mail;

import cn.hutool.core.lang.Assert;
import com.cex0.mobiai.exception.EmailException;
import com.cex0.mobiai.model.properties.EmailProperties;
import com.cex0.mobiai.service.OptionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/11 20:54
 * @Description:
 */
@Slf4j
public abstract class AbstractMailService implements MailService {

    private static final int DEFAULT_POOL_SIZE = 5;

    protected final OptionService optionService;

    private JavaMailSender cachedMailSender;

    private MailProperties cachedMailProperties;

    private String cachedFromName;

    private ExecutorService executorService;

    protected  AbstractMailService(OptionService optionService) {
        this.optionService = optionService;
    }

    @NonNull
    public ExecutorService getExecutorService() {
        if (this.executorService == null) {
            this.executorService = Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);
        }
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }


    /**
     * email服务测试连接
     */
    @Override
    public void testConnection() {
        JavaMailSender javaMailSender = getMailSender();

        if (javaMailSender instanceof JavaMailSenderImpl) {
            JavaMailSenderImpl mailSender = (JavaMailSenderImpl) javaMailSender;
            try {
                mailSender.testConnection();
            } catch (MessagingException e) {
                throw new EmailException("无法连接到邮箱服务器，请检查邮箱配置.[" + e.getMessage() + "]", e);
            }
        }
    }


    /**
     * 发送模版邮件
     *
     * @param callback
     */
    protected void sendMailTemplate(@Nullable Callback callback) {
        if (callback == null) {
            log.info("Callback is null, skip to send email");
            return;
        }

        // 检查邮件是否启用
        Boolean emailEnabled = optionService.getByPropertyOrDefault(EmailProperties.ENABLED, Boolean.class);

        if (!emailEnabled) {
            // 如果未开启
            log.info("Email has been disabled by yourself, you can re-enable it through email settings on admin page.");
            return;
        }

        // 获取mail sender
        JavaMailSender mailSender = getMailSender();
        printMailConfig();

        // 建立一个邮件helper
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage());

        // 设置发送者名
        try {
            messageHelper.setFrom(getFromAddress(mailSender));

            callback.handle(messageHelper);

            MimeMessage mimeMessage = messageHelper.getMimeMessage();

            mailSender.send(mimeMessage);

            log.info("Sent an email to [{}] successfully, subject: [{}], sent date: [{}]",
                    Arrays.toString(mimeMessage.getAllRecipients()),
                    mimeMessage.getSubject(),
                    mimeMessage.getSentDate());
        }
        catch (Exception e) {
            throw new EmailException("邮件发送失败，请检查 SMTP 服务配置是否正确", e);
        }
    }

    /**
     * 异步发送邮件（需要判断）
     *
     * @param tryToAsync    判断是否需要异步发送邮件
     * @param callback
     */
    protected void sendMailTemplate(boolean tryToAsync, @Nullable Callback callback) {
        ExecutorService executorService = getExecutorService();
        if (tryToAsync && executorService != null) {
            // 异步发送邮件
            executorService.execute(() -> sendMailTemplate(callback));
        }
        else {
            // 同步发送邮件
            sendMailTemplate(callback);
        }
    }

    /**
     * 获取JavaMailSender对象
     *
     * @return
     */
    private synchronized JavaMailSender getMailSender() {
       if (this.cachedMailSender == null) {
           MailSenderFactory mailSenderFactory = new MailSenderFactory();
           this.cachedMailSender = mailSenderFactory.getMailSender(getMailProperties());
       }

       return this.cachedMailSender;
    }


    /**
     * 获取发送邮箱
     *
     * @param javaMailSender
     * @return
     * @throws UnsupportedEncodingException 当有错误的字符编码时引发
     */
    private synchronized InternetAddress getFromAddress(@NonNull JavaMailSender javaMailSender) throws UnsupportedEncodingException {
        Assert.notNull(javaMailSender, "Java mail sender must not be null");

        if (StringUtils.isBlank(this.cachedFromName)) {
            // 设置个人账户
            this.cachedFromName = optionService.getByPropertyOfNonNull(EmailProperties.FROM_NAME).toString();
        }

        if (javaMailSender instanceof JavaMailSenderImpl) {
            JavaMailSenderImpl mailSender = (JavaMailSenderImpl) javaMailSender;
            String username = mailSender.getUsername();

            // 生成internet地址
            return new InternetAddress(username, this.cachedFromName, mailSender.getDefaultEncoding());
        }

        throw new UnsupportedOperationException("Unsupported java mail sender: " + javaMailSender.getClass().getName());
    }


    /**
     * 获取邮箱参数
     *
     * @return
     */
    private synchronized MailProperties getMailProperties() {
        if (cachedMailProperties == null) {
            // 创建mail properties
            MailProperties mailProperties = new MailProperties(log.isDebugEnabled());

            // 设置
            mailProperties.setHost(optionService.getByPropertyOrDefault(EmailProperties.HOST, String.class));
            mailProperties.setPort(optionService.getByPropertyOrDefault(EmailProperties.SSL_PORT, Integer.class));
            mailProperties.setUsername(optionService.getByPropertyOrDefault(EmailProperties.USERNAME, String.class));
            mailProperties.setPassword(optionService.getByPropertyOrDefault(EmailProperties.PASSWORD, String.class));
            mailProperties.setProtocol(optionService.getByPropertyOrDefault(EmailProperties.PROTOCOL, String.class));
            this.cachedMailProperties = mailProperties;
        }

        return this.cachedMailProperties;
    }


    /**
     * 打印邮件配置
     */
    private void printMailConfig() {
        if (!log.isDebugEnabled()) {
            return;
        }

        // get mail properties
        MailProperties mailProperties = getMailProperties();
        log.debug(mailProperties.toString());
    }

    /**
     * 清除缓存实例。
     */
    protected void clearCache() {
        this.cachedMailProperties = null;
        this.cachedFromName = null;
        this.cachedMailSender = null;
        log.debug("Cleared all mail caches");
    }




    /**
     * 消息回调。
     */
    protected  interface Callback {

        /**
         * 处理消息集。
         *
         * @param messageHelper
         * @throws Exception
         */
        void handle(@NonNull MimeMessageHelper messageHelper) throws Exception;
    }

}
