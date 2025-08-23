package com.example.msauthentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

import java.security.SecureRandom;

@Service
public class EmailService {



    @Autowired
    private JavaMailSender mailSender;



    public void sendPasswordResetEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Solicitud de restablecimiento de contraseña");
        message.setText("Esta es tu clave de un solo uso para restablecer tu contraseña: " + otp);
        mailSender.send(message);
    }
}