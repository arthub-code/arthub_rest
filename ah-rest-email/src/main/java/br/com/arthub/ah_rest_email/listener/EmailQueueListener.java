package br.com.arthub.ah_rest_email.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.arthub.ah_rest_email.config.RabbitMQConfig;
import br.com.arthub.ah_rest_email.dto.EmailRequest;
import br.com.arthub.ah_rest_email.service.EmailService;

@Service
public class EmailQueueListener {
    @Autowired
    private EmailService emailService;
    
    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void sendEmail(String payload) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            EmailRequest emailRequest = objectMapper.readValue(payload, EmailRequest.class);
            emailService.send(emailRequest);    
        } catch(Exception e) {
            System.err.println("Não foi possível converter o json em string para uma classe java: " + e.getMessage());
            System.err.println("payload: " + payload);
        }
    }
}
