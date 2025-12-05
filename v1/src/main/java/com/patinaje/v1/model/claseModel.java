package com.patinaje.v1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_clases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class claseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 50)
    @NotBlank(message = "El nombre de la clase es obligatorio")
    private String nombre;

    @Column(name = "nivel", nullable = false, length = 30)
    @NotBlank(message = "El nivel es obligatorio")
    private String nivel;  // Mini, Júnior, Adultos, etc.

    @Column(name = "dia", nullable = false, length = 20)
    @NotBlank(message = "El día es obligatorio")
    private String dia;  // Lunes, Martes, Miércoles, etc.

    @Column(name = "hora_inicio", nullable = false, length = 20)
    @NotBlank(message = "La hora de inicio es obligatoria")
    private String horaInicio;  // HH:mm

    @Column(name = "hora_fin", nullable = false, length = 20)
    @NotBlank(message = "La hora de fin es obligatoria")
    private String horaFin;  // HH:mm

    @Column(name = "capacidad", nullable = false)
    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser mayor a 0")
    private Integer capacidad;

    @Column(name = "inscritos", nullable = false)
    private Integer inscritos = 0;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "Activa";  // Activa, Inactiva, Pausada

    // Relación con instructor
    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private instructorModel instructor;
}