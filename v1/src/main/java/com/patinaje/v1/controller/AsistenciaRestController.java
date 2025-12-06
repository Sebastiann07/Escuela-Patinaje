package com.patinaje.v1.controller;

import com.patinaje.v1.model.asistenciaModel;
import com.patinaje.v1.model.userModel;
import com.patinaje.v1.model.claseModel;
import com.patinaje.v1.service.asistenciaService;
import com.patinaje.v1.service.userService;
import com.patinaje.v1.service.claseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/asistencias")
@Tag(name = "Asistencias API", description = "Operaciones REST sobre asistencias")
public class AsistenciaRestController {

    @Autowired
    private asistenciaService asistenciaService;

    @Autowired
    private userService userService;

    @Autowired
    private claseService claseService;

    @GetMapping
    @Operation(summary = "Listar asistencias")
    public ResponseEntity<List<asistenciaModel>> all() {
        return ResponseEntity.ok(asistenciaService.getAll());
    }

    @GetMapping("/alumno/{alumnoId}")
    @Operation(summary = "Obtener asistencias por alumno")
    public ResponseEntity<Object> byAlumno(@PathVariable Long alumnoId) {
        return userService.getUserById(alumnoId)
                .<ResponseEntity<Object>>map(alumno -> ResponseEntity.ok(asistenciaService.obtenerPorAlumno((userModel) alumno)))
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "Alumno no encontrado")));
    }

    @GetMapping("/clase/{claseId}")
    @Operation(summary = "Obtener asistencias por clase")
    public ResponseEntity<Object> byClase(@PathVariable Long claseId) {
        return claseService.getClaseById(claseId)
                .<ResponseEntity<Object>>map(clase -> ResponseEntity.ok(asistenciaService.obtenerPorClase((claseModel) clase)))
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "Clase no encontrada")));
    }

    @PostMapping
    @Operation(summary = "Registrar asistencia")
    public ResponseEntity<Object> create(@RequestBody asistenciaModel asistencia) {
        try {
            asistenciaService.guardar(asistencia);
            return ResponseEntity.status(201).body(asistencia);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
