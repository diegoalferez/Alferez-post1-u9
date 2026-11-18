package com.universidad.seguridad.controller;

import com.universidad.seguridad.service.TareaService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tareas")
public class TareaWebController {

    private final TareaService service;

    public TareaWebController(TareaService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String texto,
                         Authentication auth, Model model) {
        boolean esAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        boolean buscando = texto != null && !texto.isBlank();
        model.addAttribute("tareas", buscando
                ? service.buscar(texto, auth.getName(), esAdmin)
                : service.listarDe(auth.getName()));
        model.addAttribute("texto", texto);
        return "tareas/lista";
    }

    @PostMapping
    public String crear(@RequestParam String titulo,
                        @RequestParam(required = false) String descripcion,
                        Authentication auth) {
        String t = titulo == null ? "" : titulo.trim();
        if (t.isEmpty() || t.length() > 150) return "redirect:/tareas?error";
        String d = descripcion == null ? "" : descripcion.trim();
        if (d.length() > 500) d = d.substring(0, 500);
        service.crear(t, d, auth.getName());
        return "redirect:/tareas";
    }
}
