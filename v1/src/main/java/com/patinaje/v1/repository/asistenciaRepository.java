package com.patinaje.v1.repository;

import com.patinaje.v1.model.asistenciaModel;
import com.patinaje.v1.model.userModel;
import com.patinaje.v1.model.claseModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface asistenciaRepository extends JpaRepository<asistenciaModel, Long> {

    List<asistenciaModel> findByAlumno(userModel alumno);

    List<asistenciaModel> findByClase(claseModel clase);
}
