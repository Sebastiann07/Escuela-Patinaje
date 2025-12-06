package com.patinaje.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_instructores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Instructor", description = "Representa a un instructor de la escuela")
public class instructorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "nombre", nullable = false, length = 50)
    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre completo del instructor", example = "Carlos Gómez")
    String nombre;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    @Email(message = "Debe ser un correo válido")
    @NotBlank(message = "El email es obligatorio")
    @Schema(description = "Correo electrónico", example = "instructor@example.com")
    String email;

    @Column(name = "password", nullable = false, length = 100)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Schema(description = "Contraseña (solo escritura)", example = "secret123")
    String password;

    @Column(name = "telefono", nullable = true, length = 15)
    @Schema(description = "Teléfono de contacto", example = "+34123456789")
    String telefono;

    @Column(name = "especialidad", nullable = true, length = 100)
    @Schema(description = "Especialidad del instructor", example = "Artístico")
    String especialidad;  // Freestyle, Velocidad, Artístico, etc.

    @Column(name = "experiencia", nullable = true, length = 100)
    @Schema(description = "Resumen de experiencia", example = "5 años")
    String experiencia;  // Años de experiencia

    @Column(name = "estado", nullable = false, length = 20)
    @Schema(description = "Estado del instructor", example = "Activo")
    String estado = "Activo";

    @Column(name = "fecha_registro", nullable = false)
    @Schema(description = "Fecha de registro", example = "2024-01-05")
    String fechaRegistro;
}