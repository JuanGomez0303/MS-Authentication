package com.example.msauthentication.controller;

import com.example.msauthentication.model.AuthResponse;
import com.example.msauthentication.model.LoginRequest;
import com.example.msauthentication.model.RegisterRequest;
import com.example.msauthentication.service.AuthService;
import jakarta.mail.MessagingException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final String HomeBaseURL = "Frontend_URL";

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public  ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) throws MessagingException {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/validate_email/{email}")
    public ResponseEntity<Void> validateEmailAndRedirect(@PathVariable String email) {
        authService.validateEmail(email);
        return ResponseEntity.status(302).header(
                "Location", HomeBaseURL + "/login").build();
    }

}
