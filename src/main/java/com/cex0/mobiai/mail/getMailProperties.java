package com.cex0.mobiai.mail;

import com.cex0.mobiai.event.options.OptionUpdatedEvent;
import com.cex0.mobiai.service.OptionService;
import freemarker.template.Template;
import org.springframework.context.ApplicationListener;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author wodenvyoujiaoshaxiong
 * @Date: 2020/3/11 22:21
 * @Description:
 */
public class getMailProperties extends AbstractMailService implements ApplicationListener<OptionUpdatedEvent> {

    private final FreeMarkerConfigurer freeMarker;

    protected getMailProperties(FreeMarkerConfigurer freeMarker,
                                OptionService optionService) {
        super(optionService);
        this.freeMarker = freeMarker;
    }

    @Override
    public void sendTextMail(String to, String subject, String content) {
        sendMailTemplate(true, messageHelper -> {
            messageHelper.setSubject(subject);
            messageHelper.setTo(to);
            messageHelper.setText(content);
        });
    }

    @Override
    public void sendTemplateMail(String to, String subject, Map<String, Object> content, String templateName) {
        sendMailTemplate(true, messageHelper -> {
            // 使用freemarker生成消息内容
            Template template = freeMarker.getConfiguration().getTemplate(templateName);
            String contentResult = FreeMarkerTemplateUtils.processTemplateIntoString(template, content);

            messageHelper.setSubject(subject);
            messageHelper.setTo(to);
            messageHelper.setText(contentResult, true);
        });
    }

    @Override
    public void sendAttachMail(String to, String subject, Map<String, Object> content, String templateName, String attachFilePath) {
        sendMailTemplate(true, messageHelper -> {
            messageHelper.setSubject(subject);
            messageHelper.setTo(to);
            Path attachmentPath = Paths.get(attachFilePath);
            messageHelper.addAttachment(attachmentPath.getFileName().toString(), attachmentPath.toFile());
        });
    }

    @Override
    public void testConnection() {
        super.testConnection();
    }

    @Override
    public void onApplicationEvent(OptionUpdatedEvent optionUpdatedEvent) {
        // 清除缓存的java邮件发件人
        clearCache();
    }
}
