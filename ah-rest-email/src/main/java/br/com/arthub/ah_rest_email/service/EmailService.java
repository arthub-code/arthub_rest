package br.com.arthub.ah_rest_email.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import br.com.arthub.ah_rest_email.dto.EmailRequest;
import br.com.arthub.ah_rest_email.service.utils.EmailTemplateUtils;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;
    
    public void send(EmailRequest emailRequest) {
        switch (emailRequest.getEmailOrder()) {
            case CONFIRMATION:
                sendConfirmationEmail(emailRequest);
                break;
            case PASSWORD_CHANGE:
            	sendPasswordChangeEmail(emailRequest);
            	break;
            default:
                break;
        }
    }

    private void sendConfirmationEmail(EmailRequest emailRequest) {
        EmailTemplateUtils utils = new EmailTemplateUtils(); 
        Map<String, String> keyMap = new HashMap<>();

        keyMap.put("username", emailRequest.getUsername());
        keyMap.put("confirmation_link", emailRequest.getConfirmEndpoint());
        keyMap.put("copyright", "© Arthub " + LocalDate.now().getYear() + " - Todos os direitos reservados.");
        try {
            String emailBody = utils.generateEmailContent("/templates/htmls/email_confirmation.html", keyMap);
            sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailBody);
            System.out.println("Email de confirmação enviado para: " + emailRequest.getTo());
        } catch (Exception e) {
            System.err.println("Erro ao gerar o corpo do email de confirmação: " + e.getMessage());
        }
    }
    
    private void sendPasswordChangeEmail(EmailRequest emailRequest) {
        EmailTemplateUtils utils = new EmailTemplateUtils(); 
        Map<String, String> keyMap = new HashMap<>();
        keyMap.put("username", emailRequest.getUsername());
        keyMap.put("password_reset_link", emailRequest.getConfirmEndpoint());
        keyMap.put("copyright", "© Arthub " + LocalDate.now().getYear() + " - Todos os direitos reservados.");
        try {
            String emailBody = utils.generateEmailContent("/templates/htmls/email_passwordChange.html", keyMap);
            sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailBody);
            System.out.println("Email de confirmação enviado para: " + emailRequest.getTo());
        } catch (Exception e) {
            System.err.println("Erro ao gerar o corpo do email de confirmação: " + e.getMessage());
        }
    }

    private void sendEmail(String to, String subject, String body) throws Exception {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        emailSender.send(mimeMessage);
    }
}
