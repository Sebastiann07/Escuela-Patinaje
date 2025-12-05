package com.patinaje.v1.controller;

import com.patinaje.v1.model.*;
import com.patinaje.v1.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/asistencia")
public class asistenciaController {

    private final claseService claseService;
    private final userService userService;
    private final asistenciaService asistenciaService;

    public asistenciaController(claseService claseService, userService userService, asistenciaService asistenciaService) {
        this.claseService = claseService;
        this.userService = userService;
        this.asistenciaService = asistenciaService;
    }

    // Mostrar listado de clases para marcar asistencia
    @GetMapping
    public String listarClases(Model model) {
        model.addAttribute("clases", claseService.getAllClases());
        return "asistencia/clases";
    }

    // Mostrar alumnos de una clase
    @GetMapping("/marcar/{idClase}")
    public String marcarAsistencia(@PathVariable Long idClase, Model model) {

        claseModel clase = claseService.getClaseById(idClase).orElse(null);
        List<userModel> alumnos = userService.getAllUsers();

        model.addAttribute("clase", clase);
        model.addAttribute("alumnos", alumnos);

        return "asistencia/marcar";
    }

    // Guardar asistencia
    @PostMapping("/guardar")
    public String guardarAsistencia(@RequestParam Long claseId,
                                    @RequestParam Long alumnoId,
                                    @RequestParam String estado) {

        asistenciaModel asistencia = new asistenciaModel();
        asistencia.setClase(claseService.getClaseById(claseId).orElse(null));
        asistencia.setAlumno(userService.getUserById(alumnoId).orElse(null));
        asistencia.setEstado(estado);

        asistenciaService.guardar(asistencia);

        return "redirect:/asistencia/marcar/" + claseId;
    }

    // Reporte por alumno
    @GetMapping("/reporte/{idAlumno}")
    public String reporteAlumno(@PathVariable Long idAlumno, Model model) {

        userModel alumno = userService.getUserById(idAlumno).orElse(null);
        List<asistenciaModel> asistencias = asistenciaService.obtenerPorAlumno(alumno);

        model.addAttribute("alumno", alumno);
        model.addAttribute("asistencias", asistencias);

        return "asistencia/reporte";
    }
}
