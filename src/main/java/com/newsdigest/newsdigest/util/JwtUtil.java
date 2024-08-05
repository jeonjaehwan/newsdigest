package com.newsdigest.newsdigest.util;

import com.newsdigest.newsdigest.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(CustomUserDetails userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }


    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public boolean isTokenValid(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public long getExpirationTimeFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}


