package com.universidad.seguridad.controller;

import com.universidad.seguridad.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public ApiAuthController(AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    public record LoginRequest(String email, String contrasenia) {}
    public record LoginResponse(String token, String email, String rol) {}

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        // Mismo AuthenticationManager / UserDetailsService / BCrypt de la Parte 1:
        // solo cambia lo que se devuelve (un token en vez de una cookie de sesión).
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.contrasenia()));
        String rol = auth.getAuthorities().iterator().next().getAuthority();
        String token = jwtUtil.generarToken(req.email(), rol);
        return new LoginResponse(token, req.email(), rol);
    }

    // Credenciales incorrectas -> 401 con JSON, sin emitir token
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> credencialesInvalidas(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Credenciales inválidas"));
    }
}
