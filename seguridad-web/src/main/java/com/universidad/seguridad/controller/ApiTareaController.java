package com.universidad.seguridad.controller;

import com.universidad.seguridad.model.Tarea;
import com.universidad.seguridad.service.TareaService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class ApiTareaController {

    private final TareaService service;

    public ApiTareaController(TareaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Tarea> misTareas(Authentication auth) {
        // Authentication llega poblado por JwtAuthFilter, igual que si la
        // petición viniera autenticada por sesión: auth.getName() es el correo.
        return service.listarDe(auth.getName());
    }
}
