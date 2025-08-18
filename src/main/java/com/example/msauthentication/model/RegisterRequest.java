package com.example.msauthentication.model;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false,name = "psw_hash")
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    private String name;

    @Column(nullable = false, name = "phone")
    private String phoneNumber;

    @Column(nullable = false)
    private String role;

}
