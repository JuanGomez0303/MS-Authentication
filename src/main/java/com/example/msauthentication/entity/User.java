package com.example.msauthentication.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table (name = "users")
@Builder
public class User {

    @Id
    @GeneratedValue
    private Long id;

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

    @Column(nullable = false, name = "registration_time")
    private LocalDateTime registrationTime;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ForgotPassword forgotPassword;
}
