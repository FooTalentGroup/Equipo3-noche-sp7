package com.stockia.stockia.services.Impl;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.stockia.stockia.exceptions.EmailSendingException;
import com.stockia.stockia.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final SpringTemplateEngine templateEngine;
    @Value("${sendgrid.api-key}")
    private String sendGridKey;

    @Override
    public void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            // Procesar plantilla HTML
            Context context = new Context();
            context.setVariables(variables);
            String htmlContent = templateEngine.process(templateName, context);

            // Construir el mail
            Email from = new Email("Stockiainc@gmail.com");
            Email toEmail = new Email(to);

            Content content = new Content("text/html", htmlContent);
            Mail mail = new Mail(from, subject, toEmail, content);

            // Usar la API key de SendGrid desde el entorno (.env en Render)
            SendGrid sg = new SendGrid(sendGridKey);

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            // Logs opcionales
            log.info("Email sent. Status: {}", response.getStatusCode());

            if (response.getStatusCode() >= 400) {
                if (response.getStatusCode() == 401) {
                    throw new EmailSendingException("SendGrid authentication error: API key inválida o remitente no autorizado");
                }
                throw new EmailSendingException("SendGrid error: " + response.getStatusCode());
            }

        } catch (IOException e) {
            throw new EmailSendingException("Failed to send email via SendGrid", e);
        }
    }


}
