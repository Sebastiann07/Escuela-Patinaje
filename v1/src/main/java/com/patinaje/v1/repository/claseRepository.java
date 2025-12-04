package com.patinaje.v1.repository;

import com.patinaje.v1.model.claseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface claseRepository extends JpaRepository<claseModel, Long> {
    
    // Buscar clases por nivel
    List<claseModel> findByNivel(String nivel);
    
    // Buscar clases por día
    List<claseModel> findByDia(String dia);
    
    // Buscar clases por estado
    List<claseModel> findByEstado(String estado);
    
    // Buscar clases por instructor
    List<claseModel> findByInstructorId(Long instructorId);
    
    // Buscar clases activas
    List<claseModel> findByEstadoAndNivel(String estado, String nivel);
}