package br.com.api.ecommerce_email.services;

import br.com.api.ecommerce_email.models.EmailLog;
import br.com.api.ecommerce_email.models.EmailTemplate;
import br.com.api.ecommerce_email.models.enums.StatusEnum;
import br.com.api.ecommerce_email.repositories.EmailLogRepository;
import br.com.api.ecommerce_email.repositories.EmailTemplateRepository;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value(value = "${spring.mail.username}")
    private String emailFrom;

    @Autowired
    private EmailLogRepository logRepository;

    @Autowired
    private EmailTemplateRepository templateRepository;

    public void sendEmail(String eventType, Map<String, Object> data) {
        EmailTemplate template = templateRepository.findByEventType(eventType)
                .orElseThrow(() -> {
                    log.warn("Template não encontrado para o evento");
                    return new AmqpRejectAndDontRequeueException("Template not found: " + eventType);
                });

        if (template.getHtmlContent() == null){
            log.error("Template está sem conteúdo HTML");
            throw new RuntimeException("Template has no content: " + eventType);
        }

        String emailTo = (String) data.get("emailTo");

        EmailLog emailLog = new EmailLog();
        emailLog.setEmailFrom(emailFrom);
        emailLog.setEmailTo(emailTo);
        emailLog.setEventType(eventType);
        emailLog.setSentAt(LocalDateTime.now());

        try {
            String html = compileTemplate(template.getHtmlContent(), data);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(emailTo);
            helper.setSubject(template.getSubject());
            helper.setText(html, true);

            mailSender.send(message);
            emailLog.setStatus(StatusEnum.SENT);
            log.info("E-mail enviado com sucesso");
        } catch (Exception ex) {
            emailLog.setStatus(StatusEnum.ERROR);
            log.error("Erro ao disparar e-mail", ex);
        } finally {
            logRepository.save(emailLog);
        }
    }

    private String compileTemplate(String htmlContent, Map<String, Object> variables) {
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(new StringReader(htmlContent), "email-template");

        StringWriter writer = new StringWriter();
        mustache.execute(writer, variables);

        return writer.toString();
    }
}