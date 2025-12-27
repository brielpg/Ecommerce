package br.com.api.ecommerce_email.services;

import br.com.api.ecommerce_email.models.EmailLog;
import br.com.api.ecommerce_email.models.EmailTemplate;
import br.com.api.ecommerce_email.models.dtos.EmailDto;
import br.com.api.ecommerce_email.models.enums.StatusEnum;
import br.com.api.ecommerce_email.repositories.EmailLogRepository;
import br.com.api.ecommerce_email.repositories.EmailTemplateRepository;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import jakarta.mail.internet.MimeMessage;
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
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value(value = "${spring.mail.username}")
    private String emailFrom;

    @Autowired
    private EmailLogRepository logRepository;

    @Autowired
    private EmailTemplateRepository templateRepository;

    public void sendEmail(EmailDto dto) {
        EmailLog emailLog = dtoToEntity(dto);

        try {
            EmailTemplate template = templateRepository.findByName(dto.templateName())
                    .orElseThrow(() -> new RuntimeException("Template not found"));

            String html = compileTemplate(template.getHtmlContent(), dto.variables());

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(dto.emailTo());
            helper.setSubject(dto.subject());
            helper.setText(html, true);

            mailSender.send(message);
            emailLog.setStatus(StatusEnum.SENT);
        } catch (Exception e) {
            emailLog.setStatus(StatusEnum.ERROR);
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

    private EmailLog dtoToEntity(EmailDto dto){
        EmailLog emailLog = new EmailLog();
        emailLog.setEmailFrom(emailFrom);
        emailLog.setEmailTo(dto.emailTo());
        emailLog.setSubject(dto.subject());
        emailLog.setTemplateName(dto.templateName());
        emailLog.setSentAt(LocalDateTime.now());

        return emailLog;
    }
}