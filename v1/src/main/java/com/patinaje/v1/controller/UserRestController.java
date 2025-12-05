package com.patinaje.v1.controller;

import com.patinaje.v1.model.userModel;
import com.patinaje.v1.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users API", description = "CRUD REST para usuarios")
public class UserRestController {

    @Autowired
    private userService userService;

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Devuelve todos los usuarios")
    public ResponseEntity<List<userModel>> findAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado", content = @Content(schema = @Schema(implementation = userModel.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        return userService.getUserById(id)
                .<ResponseEntity<Object>>map(u -> ResponseEntity.ok(u))
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado")));
    }

    @PostMapping
    @Operation(summary = "Crear usuario")
    @ApiResponse(responseCode = "201", description = "Usuario creado", content = @Content(schema = @Schema(implementation = userModel.class)))
    public ResponseEntity<userModel> create(@RequestBody userModel user) {
        userModel created = userService.createUser(user);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody userModel userDetails) {
        userModel updated = userService.updateUser(id, userDetails);
        if (updated == null) return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (!deleted) return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
        return ResponseEntity.ok(Map.of("deleted", true));
    }
}
