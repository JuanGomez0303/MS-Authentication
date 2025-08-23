package com.example.msauthentication.controller;

import com.example.msauthentication.model.AuthResponse;
import com.example.msauthentication.model.LoginRequest;
import com.example.msauthentication.model.RegisterRequest;
import com.example.msauthentication.service.AuthService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public  ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
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

}
