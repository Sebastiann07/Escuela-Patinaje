package com.patinaje.v1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "User", description = "Modelo que representa un usuario de la escuela")
public class userModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 50)
    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre completo del usuario", example = "María Pérez")
    private String name;

    @Column(name = "e_mail", nullable = false, unique = true, length = 100)
    @Email(message = "Debe ser un correo válido")
    @NotBlank(message = "El email es obligatorio")
    @Schema(description = "Correo electrónico", example = "maria@example.com")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Schema(description = "Contraseña (solo escritura)", example = "secret123", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @Column(name = "telefono", length = 15)
    @Schema(description = "Teléfono de contacto", example = "+34123456789")
    private String phone;

    @Column(name = "fecha_nacimiento")
    @Schema(description = "Fecha de nacimiento del usuario", example = "2005-09-15")
    private LocalDate fechaNacimiento;

    @Column(name = "genero", length = 20)
    @Schema(description = "Género del usuario", example = "Femenino")
    private String genero;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Role rol = Role.ALUMNO; // Por defecto es alumno

    @Column(name = "activo")
    @Schema(description = "Indica si el usuario está activo", example = "true")
    private boolean activo = true;

    // Enum para los roles
    public enum Role {
        ADMIN,
        INSTRUCTOR,
        ALUMNO
    }
}