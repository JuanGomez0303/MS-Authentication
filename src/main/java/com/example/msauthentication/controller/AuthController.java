package com.example.msauthentication.controller;

import com.example.msauthentication.entity.User;
import com.example.msauthentication.model.AuthResponse;
import com.example.msauthentication.model.LoginRequest;
import com.example.msauthentication.model.RegisterRequest;
import com.example.msauthentication.model.UserDTO;
import com.example.msauthentication.service.AuthService;
import jakarta.mail.MessagingException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ms-auth/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final String HomeBaseURL = "Frontend_URL";

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) throws MessagingException {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/validate_email/{email}")
    public ResponseEntity<Void> validateEmailAndRedirect(@PathVariable String email) {
        authService.validateEmail(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(authService.getUserFromId(id));

    }

    @PostMapping("/recover_password/{email}")
    public ResponseEntity<AuthResponse> recovery(@PathVariable String email) {
        return ResponseEntity.ok(authService.recovery(email));
    }

    @PostMapping("/reset_password/{otp}/{newPassword}")
    public ResponseEntity<AuthResponse> resetPassword(@PathVariable String otp, @PathVariable String newPassword) {
        return ResponseEntity.ok(authService.resetPassword(otp, newPassword));
    }

    @PostMapping("/validate_otp/{otp}")
    public ResponseEntity<AuthResponse> validateOtp(@PathVariable String otp) {
        return ResponseEntity.ok(authService.validateOtp(otp));
    }

    @PostMapping("/token/getUser")
    public ResponseEntity<UserDTO> getUserFromToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UserDTO userDTO = authService.getUserFromToken(token);
        if (userDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/token/validate")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = authService.validateToken(token);
        if (userId == null) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }
        return ResponseEntity.ok(userId);
    }
}
