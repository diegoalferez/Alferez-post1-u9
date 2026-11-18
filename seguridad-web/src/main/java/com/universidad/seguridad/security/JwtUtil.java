package com.universidad.seguridad.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey clave;
    private final long expiracionMs;

    // La clave se lee de application.properties / variable de entorno JWT_SECRET,
    // nunca queda escrita en el código fuente.
    public JwtUtil(@Value("${jwt.secret}") String secreto,
                   @Value("${jwt.expiration-ms}") long expiracionMs) {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.expiracionMs = expiracionMs;
    }

    public String generarToken(String email, String rol) {
        return Jwts.builder()
                .subject(email)
                .claim("rol", rol)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiracionMs))
                .signWith(clave)
                .compact();
    }

    public String extraerEmail(String token) {
        return parsearClaims(token).getSubject();
    }

    public String extraerRol(String token) {
        return parsearClaims(token).get("rol", String.class);
    }

    public boolean esValido(String token) {
        try {
            parsearClaims(token); // lanza excepción si la firma o la expiración no son válidas
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;         // firma inválida, token expirado o malformado
        }
    }

    private Claims parsearClaims(String token) {
        return Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
