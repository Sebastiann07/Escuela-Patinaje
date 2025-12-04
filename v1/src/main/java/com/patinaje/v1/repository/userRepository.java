package com.patinaje.v1.repository;

import com.patinaje.v1.model.userModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface userRepository extends JpaRepository<userModel, Long> {
    
    // Buscar usuario por email (para login)
    Optional<userModel> findByEmail(String email);
    
    // Verificar si existe un email
    boolean existsByEmail(String email);
}