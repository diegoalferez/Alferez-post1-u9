package com.universidad.seguridad.service;

import com.universidad.seguridad.model.Usuario;
import com.universidad.seguridad.repository.UsuarioRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Transactional
    public void registrar(Usuario usuario) {
        if (repo.existsByEmail(usuario.getEmail()))
            throw new RuntimeException("El correo ya está registrado");
        usuario.setId(null);                                  // siempre un usuario nuevo
        usuario.setActivo(true);
        usuario.setContrasenia(encoder.encode(usuario.getContrasenia()));
        usuario.setRol("ROLE_USER");                          // rol por defecto
        repo.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario obtenerPorEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado: " + email));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return repo.findAll();
    }
}
