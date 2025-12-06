package com.patinaje.v1.controller;

import com.patinaje.v1.model.instructorModel;
import com.patinaje.v1.service.instructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/instructores")
@Tag(name = "Instructores API", description = "CRUD REST para instructores")
public class InstructorRestController {

    @Autowired
    private instructorService instructorService;

    @GetMapping
    @Operation(summary = "Listar instructores")
    public ResponseEntity<List<instructorModel>> all() {
        return ResponseEntity.ok(instructorService.getAllInstructores());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener instructor por id")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return instructorService.getInstructorById(id)
                .<ResponseEntity<Object>>map(i -> ResponseEntity.ok(i))
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "Instructor no encontrado")));
    }

    @PostMapping
    @Operation(summary = "Crear instructor")
    public ResponseEntity<instructorModel> create(@RequestBody instructorModel instructor) {
        instructorModel created = instructorService.createInstructor(instructor);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar instructor")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody instructorModel details) {
        instructorModel updated = instructorService.updateInstructor(id, details);
        if (updated == null) return ResponseEntity.status(404).body(Map.of("error", "Instructor no encontrado"));
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar instructor")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        boolean ok = instructorService.deleteInstructor(id);
        if (!ok) return ResponseEntity.status(404).body(Map.of("error", "Instructor no encontrado"));
        return ResponseEntity.ok(Map.of("deleted", true));
    }
}
