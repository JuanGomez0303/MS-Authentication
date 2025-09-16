package com.example.msauthentication.service;

import com.example.msauthentication.entity.User;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import java.util.Date;

@Service
public class JwtService {
    private static final String SECRET_KEY = "477725a156d18b6f3ed04a0b6f7f817d1bd1b1cee2868eebf4befc58c1a4c71e";
    private static final long EXPIRATION_TIME = 86400000; // 1 day in ms

    public String getToken(User user) {
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
