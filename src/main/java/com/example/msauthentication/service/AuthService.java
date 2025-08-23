package com.example.msauthentication.service;


import com.example.msauthentication.entity.User;
import com.example.msauthentication.model.AuthResponse;
import com.example.msauthentication.model.LoginRequest;
import com.example.msauthentication.model.RegisterRequest;
import com.example.msauthentication.repository.userRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final userRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    private final String validationURL = "URL_DE_VALIDACION_DEL_FRONTEND";

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtService.getToken(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse register(RegisterRequest request) throws MessagingException {
        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword())) // Ensure to encode the password in a real application
            .email(request.getEmail())
            .role(request.getRole())
            .name(request.getName())
            .phoneNumber(request.getPhoneNumber())
            .registrationTime(LocalDateTime.now())
            .status("NOT_VALIDATED")
            .build();

        emailService.sendValidationEmail(user.getEmail(),validationURL + "/" + user.getEmail());

        userRepository.save(user);

        String token = jwtService.getToken(user);

        return AuthResponse.builder()
            .token(token)
            .build();
    }

    public AuthResponse validateEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return AuthResponse.builder()
                    .message("Usuario con ese email no encontrado")
                    .build();
        }

        User user = userOptional.get();
        user.setStatus("VALIDATED");
        userRepository.save(user);
        
        return AuthResponse.builder()
                .message("Email validado correctamente")
                .build();
    }

}
