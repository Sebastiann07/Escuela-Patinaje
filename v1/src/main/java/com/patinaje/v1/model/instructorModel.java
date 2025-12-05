package com.patinaje.v1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_instructores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class instructorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "nombre", nullable = false, length = 50)
    @NotBlank(message = "El nombre es obligatorio")
    String nombre;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    @Email(message = "Debe ser un correo válido")
    @NotBlank(message = "El email es obligatorio")
    String email;

    @Column(name = "telefono", nullable = true, length = 15)
    String telefono;

    @Column(name = "especialidad", nullable = true, length = 100)
    String especialidad;  // Freestyle, Velocidad, Artístico, etc.

    @Column(name = "experiencia", nullable = true, length = 100)
    String experiencia;  // Años de experiencia

    @Column(name = "estado", nullable = false, length = 20)
    String estado = "Activo";

    @Column(name = "fecha_registro", nullable = false)
    String fechaRegistro;
}