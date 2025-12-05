package com.patinaje.v1.controller;

import com.patinaje.v1.model.claseModel;
import com.patinaje.v1.service.claseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clases")
@Tag(name = "Clases API", description = "CRUD REST para clases")
public class ClaseRestController {

    @Autowired
    private claseService claseService;

    @GetMapping
    @Operation(summary = "Listar clases")
    public ResponseEntity<List<claseModel>> all() {
        return ResponseEntity.ok(claseService.getAllClases());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener clase por id")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return claseService.getClaseById(id)
                .<ResponseEntity<Object>>map(c -> ResponseEntity.ok(c))
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "Clase no encontrada")));
    }

    @PostMapping
    @Operation(summary = "Crear clase")
    public ResponseEntity<Object> create(@RequestBody claseModel clase) {
        try {
            claseModel created = claseService.createClase(clase);
            return ResponseEntity.status(201).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar clase")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody claseModel details) {
        try {
            claseModel updated = claseService.updateClase(id, details);
            if (updated == null) return ResponseEntity.status(404).body(Map.of("error", "Clase no encontrada"));
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar clase")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        try {
            boolean ok = claseService.deleteClase(id);
            if (!ok) return ResponseEntity.status(404).body(Map.of("error", "Clase no encontrada"));
            return ResponseEntity.ok(Map.of("deleted", true));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
