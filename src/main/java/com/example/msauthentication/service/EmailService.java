package com.example.msauthentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendValidationEmail(String to, String validationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Vericación de correo electrónico");
        message.setText("Para validar tu cuenta de Firteps ingresa al siguiente enlace: " + validationLink);
        mailSender.send(message);
    }

}
