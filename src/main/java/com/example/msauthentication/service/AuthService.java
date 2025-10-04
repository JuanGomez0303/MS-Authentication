package com.example.msauthentication.service;


import com.example.msauthentication.entity.ForgotPassword;
import com.example.msauthentication.entity.User;
import com.example.msauthentication.model.AuthResponse;
import com.example.msauthentication.model.LoginRequest;
import com.example.msauthentication.model.RegisterRequest;
import com.example.msauthentication.model.UserDTO;
import com.example.msauthentication.repository.ForgotPasswordRepository;
import com.example.msauthentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final JwtService jwtService;
    private final EmailService emailService;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int TOKEN_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    public AuthResponse login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        if (userOptional.isEmpty()) {
            return AuthResponse.builder()
                    .message("Nombre de usuario no encnontrado")
                    .build();
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return AuthResponse.builder()
                    .message("Contraseña incorrecta")
                    .build();
        }

        String token = jwtService.getToken(user);

        return AuthResponse.builder()
                .token(token)
                .message("Login exitoso")
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword())) // Ensure to encode the password in a real application
            .email(request.getEmail())
            .role(request.getRole())
            .name(request.getName())
            .phoneNumber(request.getPhoneNumber())
            .registrationTime(LocalDateTime.now())
            .build();

        userRepository.save(user);

        String token = jwtService.getToken(user);

        return AuthResponse.builder()
            .token(token)
            .message("Usuario registrado exitosamente")
            .build();
    }

    public AuthResponse recovery(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return AuthResponse.builder()
                    .message("Correo no registrado")
                    .build();
        }

        User user = userOptional.get();

        String otp = generateResetOTP();

        emailService.sendPasswordResetEmail(user.getEmail(), otp);


        ForgotPassword fp = ForgotPassword.builder()
                .user(user)
                .otp(otp)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();

        forgotPasswordRepository.save(fp);

        return AuthResponse.builder().message("correo enviado").build();

    }

    public String generateResetOTP() {
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            token.append(CHARACTERS.charAt(index));
        }
        return token.toString();
    }



    public AuthResponse resetPassword(String otp, String newPassword) {
        ForgotPassword fp = forgotPasswordRepository.findByOtp(otp)
                .orElse(null);

        if (fp == null) {
            return AuthResponse.builder()
                    .message("OTP inválido")
                    .build();
        }

        if (fp.getExpiryDate().isBefore(LocalDateTime.now())) {
            forgotPasswordRepository.delete(fp);
            return AuthResponse.builder()
                    .message("OTP ha expirado")
                    .build();
        }

        User user = fp.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        forgotPasswordRepository.delete(fp);

        return AuthResponse.builder()
                .message("Contraseña restablecida con éxito")
                .build();
    }

    public AuthResponse validateOtp(String otp) {
        ForgotPassword fp = forgotPasswordRepository.findByOtp(otp)
                .orElse(null);

        if (fp == null) {
            return AuthResponse.builder()
                    .message("Invalid OTP")
                    .build();
        }

        if (fp.getExpiryDate().isBefore(LocalDateTime.now())) {
            forgotPasswordRepository.delete(fp);
            return AuthResponse.builder()
                    .message("OTP has expired")
                    .build();
        }

        return AuthResponse.builder()
                .message("OTP Correcto")
                .build();
    }

    public UserDTO getUserFromToken(String token) {
        String userId = jwtService.validateToken(token);
        User user = null;
        try {
            user = userRepository.findById(userId).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
        if (user == null) {
            return null;
        }
        return UserDTO.fromEntity(user);
    }

    public String validateToken(String token) {
        String userId = jwtService.validateToken(token);
        User user = null;
        try {
            user = userRepository.findById(userId).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
        if (user == null) {
            return null;
        }
        return userId;
    }
}
