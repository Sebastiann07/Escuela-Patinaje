package com.patinaje.v1.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_asistencia")
@Data
@NoArgsConstructor
@Schema(name = "Asistencia", description = "Registro de asistencia a una clase")
public class asistenciaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Fecha de la clase
    @Schema(description = "Fecha de la clase", example = "2025-12-05")
    private LocalDate fecha = LocalDate.now();

    // Estado: Presente / Ausente
    @Column(nullable = false)
    @Schema(description = "Estado de la asistencia", example = "Presente")
    private String estado;

    // Alumno
    @ManyToOne
    @JoinColumn(name = "alumno_id", nullable = false)
    @Schema(description = "Alumno asociado a la asistencia")
    private userModel alumno;

    // Clase
    @ManyToOne
    @JoinColumn(name = "clase_id", nullable = false)
    @Schema(description = "Clase asociada a la asistencia")
    private claseModel clase;
}
