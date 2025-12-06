package com.patinaje.v1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.patinaje.v1.repository.claseRepository;
import com.patinaje.v1.model.claseModel;

@Service
public class claseService {
    
    @Autowired
    private claseRepository claseRepository;

    // Obtener todas las clases
    public List<claseModel> getAllClases() {
        return claseRepository.findAll();
    }

    // Obtener una clase por ID
    public Optional<claseModel> getClaseById(Long id) {
        return claseRepository.findById(id);
    }

    // Crear una nueva clase
    @Transactional
    public claseModel createClase(claseModel clase) {
        // Validar que la capacidad sea mayor que 0
        if (clase.getCapacidad() == null || clase.getCapacidad() < 1) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a 0");
        }
        
        // Asegurar que inscritos inicie en 0
        if (clase.getInscritos() == null) {
            clase.setInscritos(0);
        }
        
        // Asegurar que tenga un estado
        if (clase.getEstado() == null || clase.getEstado().isEmpty()) {
            clase.setEstado("Activa");
        }
        
        return claseRepository.save(clase);
    }

    // Actualizar una clase
    @Transactional
    public claseModel updateClase(Long id, claseModel claseDetails) {
        Optional<claseModel> claseOpt = claseRepository.findById(id);
        
        if (claseOpt.isEmpty()) {
            return null;
        }
        
        claseModel existingClase = claseOpt.get();
        
        // Actualizar solo los campos que no son nulos
        if (claseDetails.getNombre() != null && !claseDetails.getNombre().trim().isEmpty()) {
            existingClase.setNombre(claseDetails.getNombre());
        }
        if (claseDetails.getNivel() != null && !claseDetails.getNivel().trim().isEmpty()) {
            existingClase.setNivel(claseDetails.getNivel());
        }
        if (claseDetails.getDia() != null && !claseDetails.getDia().trim().isEmpty()) {
            existingClase.setDia(claseDetails.getDia());
        }
        if (claseDetails.getHoraInicio() != null && !claseDetails.getHoraInicio().trim().isEmpty()) {
            existingClase.setHoraInicio(claseDetails.getHoraInicio());
        }
        if (claseDetails.getHoraFin() != null && !claseDetails.getHoraFin().trim().isEmpty()) {
            existingClase.setHoraFin(claseDetails.getHoraFin());
        }
        if (claseDetails.getCapacidad() != null && claseDetails.getCapacidad() > 0) {
            // Validar que la nueva capacidad no sea menor que los inscritos actuales
            if (claseDetails.getCapacidad() < existingClase.getInscritos()) {
                throw new IllegalArgumentException("La capacidad no puede ser menor que el número actual de inscritos (" + existingClase.getInscritos() + ")");
            }
            existingClase.setCapacidad(claseDetails.getCapacidad());
        }
        if (claseDetails.getDescripcion() != null) {
            existingClase.setDescripcion(claseDetails.getDescripcion());
        }
        if (claseDetails.getEstado() != null && !claseDetails.getEstado().trim().isEmpty()) {
            existingClase.setEstado(claseDetails.getEstado());
        }
        if (claseDetails.getInstructor() != null) {
            existingClase.setInstructor(claseDetails.getInstructor());
        }
        
        return claseRepository.save(existingClase);
    }

    // Eliminar una clase
    @Transactional
    public boolean deleteClase(Long id) {
        if (claseRepository.existsById(id)) {
            // Validar que no tenga alumnos inscritos antes de eliminar
            Optional<claseModel> claseOpt = claseRepository.findById(id);
            if (claseOpt.isPresent() && claseOpt.get().getInscritos() > 0) {
                throw new IllegalStateException("No se puede eliminar una clase con alumnos inscritos");
            }
            
            claseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Obtener clases por nivel
    public List<claseModel> getClasesByNivel(String nivel) {
        return claseRepository.findByNivel(nivel);
    }

    // Obtener clases por día
    public List<claseModel> getClasesByDia(String dia) {
        return claseRepository.findByDia(dia);
    }

    // Obtener clases activas
    public List<claseModel> getClasesActivas() {
        return claseRepository.findByEstado("Activa");
    }

    // Obtener clases por instructor
    public List<claseModel> getClasesByInstructor(Long instructorId) {
        return claseRepository.findByInstructorId(instructorId);
    }

    // Aumentar inscritos
    @Transactional
    public boolean aumentarInscritos(Long claseId) {
        Optional<claseModel> claseOpt = claseRepository.findById(claseId);
        
        if (claseOpt.isEmpty()) {
            return false;
        }
        
        claseModel clase = claseOpt.get();
        
        if (clase.getInscritos() >= clase.getCapacidad()) {
            throw new IllegalStateException("La clase ha alcanzado su capacidad máxima");
        }
        
        clase.setInscritos(clase.getInscritos() + 1);
        claseRepository.save(clase);
        return true;
    }

    // Disminuir inscritos
    @Transactional
    public boolean disminuirInscritos(Long claseId) {
        Optional<claseModel> claseOpt = claseRepository.findById(claseId);
        
        if (claseOpt.isEmpty()) {
            return false;
        }
        
        claseModel clase = claseOpt.get();
        
        if (clase.getInscritos() <= 0) {
            return false;
        }
        
        clase.setInscritos(clase.getInscritos() - 1);
        claseRepository.save(clase);
        return true;
    }
    
    // Verificar si una clase tiene disponibilidad
    public boolean tieneDisponibilidad(Long claseId) {
        Optional<claseModel> claseOpt = claseRepository.findById(claseId);
        return claseOpt.isPresent() && claseOpt.get().getInscritos() < claseOpt.get().getCapacidad();
    }
}