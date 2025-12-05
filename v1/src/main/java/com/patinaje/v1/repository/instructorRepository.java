package com.patinaje.v1.repository;

import com.patinaje.v1.model.instructorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface instructorRepository extends JpaRepository<instructorModel, Long> {
    
    // Buscar instructor por email
    Optional<instructorModel> findByEmail(String email);
    
    // Verificar si existe un email
    boolean existsByEmail(String email);
    
    // Buscar instructores activos
    List<instructorModel> findByEstado(String estado);
    
    // Buscar por especialidad
    List<instructorModel> findByEspecialidad(String especialidad);
}