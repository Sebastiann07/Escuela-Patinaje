package com.patinaje.v1.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_asistencia")
@Data
@NoArgsConstructor
public class asistenciaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Fecha de la clase
    private LocalDate fecha = LocalDate.now();

    // Estado: Presente / Ausente
    @Column(nullable = false)
    private String estado;

    // Alumno
    @ManyToOne
    @JoinColumn(name = "alumno_id", nullable = false)
    private userModel alumno;

    // Clase
    @ManyToOne
    @JoinColumn(name = "clase_id", nullable = false)
    private claseModel clase;
}
