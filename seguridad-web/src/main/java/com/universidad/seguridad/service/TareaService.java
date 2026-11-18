package com.universidad.seguridad.service;

import com.universidad.seguridad.model.Tarea;
import com.universidad.seguridad.model.Usuario;
import com.universidad.seguridad.repository.TareaRepository;
import com.universidad.seguridad.repository.UsuarioRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TareaService {

    private final TareaRepository repo;
    private final UsuarioRepository usuarios;

    public TareaService(TareaRepository repo, UsuarioRepository usuarios) {
        this.repo = repo;
        this.usuarios = usuarios;
    }

    // ADMIN puede listar las de cualquiera; USER solo las suyas.
    // authentication.name es el correo tanto con sesión como con JWT.
    @PreAuthorize("hasRole('ADMIN') or #email == authentication.name")
    @Transactional(readOnly = true)
    public List<Tarea> listarDe(String email) {
        return repo.findByPropietarioEmail(email);
    }

    // ADMIN busca en todas las tareas; USER solo entre las suyas
    @Transactional(readOnly = true)
    public List<Tarea> buscar(String texto, String email, boolean esAdmin) {
        return esAdmin ? repo.buscarPorTitulo(texto)            // @Query parametrizado: seguro
                       : repo.buscarPorTituloDe(email, texto);
    }

    @Transactional
    public Tarea crear(String titulo, String descripcion, String email) {
        Usuario u = usuarios.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado: " + email));
        Tarea t = new Tarea();
        t.setTitulo(titulo);
        t.setDescripcion(descripcion);
        t.setPropietario(u);
        return repo.save(t);
    }
}
