package com.cex0.mobiai.mail;

import java.util.Map;

/**
 * 邮件服务接口
 *
 * @author Cex0
 * @date 2020/03/11
 */
public interface MailService {

    /**
     * 发送简单邮件
     *
     * @param to        收件人
     * @param subject   主题
     * @param content   内容
     */
    void sendTextMail(String to, String subject, String content);


    /**
     * 用html发送电子邮件
     *
     * @param to
     * @param subject
     * @param content
     * @param templateName
     */
    void sendTemplateMail(String to, String subject, Map<String, Object> content, String templateName);


    /**
     * 发送带有附件的邮件
     *
     * @param to             收件人
     * @param subject        主题
     * @param content        内容
     * @param templateName   模板名称
     * @param attachFilePath 附件完整路径名
     */
    void sendAttachMail(String to, String subject, Map<String, Object> content, String templateName, String attachFilePath);


    /**
     * 测试电子邮件服务器连接。
     */
    void testConnection();
}
