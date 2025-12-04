package com.patinaje.v1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patinaje.v1.repository.claseRepository;
import com.patinaje.v1.model.claseModel;

@Service
public class claseService {
    
    @Autowired
    claseRepository claseRepository;

    // Obtener todas las clases
    public List<claseModel> getAllClases() {
        return claseRepository.findAll();
    }

    // Obtener una clase por ID
    public Optional<claseModel> getClaseById(Long id) {
        return claseRepository.findById(id);
    }

    // Crear una nueva clase
    public claseModel createClase(claseModel clase) {
        return claseRepository.save(clase);
    }

    // Actualizar una clase
    public claseModel updateClase(Long id, claseModel claseDetails) {
        Optional<claseModel> clase = claseRepository.findById(id);
        
        if (clase.isPresent()) {
            claseModel existingClase = clase.get();
            
            if (claseDetails.getNombre() != null) {
                existingClase.setNombre(claseDetails.getNombre());
            }
            if (claseDetails.getNivel() != null) {
                existingClase.setNivel(claseDetails.getNivel());
            }
            if (claseDetails.getDia() != null) {
                existingClase.setDia(claseDetails.getDia());
            }
            if (claseDetails.getHoraInicio() != null) {
                existingClase.setHoraInicio(claseDetails.getHoraInicio());
            }
            if (claseDetails.getHoraFin() != null) {
                existingClase.setHoraFin(claseDetails.getHoraFin());
            }
            if (claseDetails.getCapacidad() > 0) {
                existingClase.setCapacidad(claseDetails.getCapacidad());
            }
            if (claseDetails.getDescripcion() != null) {
                existingClase.setDescripcion(claseDetails.getDescripcion());
            }
            if (claseDetails.getEstado() != null) {
                existingClase.setEstado(claseDetails.getEstado());
            }
            if (claseDetails.getInstructor() != null) {
                existingClase.setInstructor(claseDetails.getInstructor());
            }
            
            return claseRepository.save(existingClase);
        }
        
        return null;
    }

    // Eliminar una clase
    public boolean deleteClase(Long id) {
        if (claseRepository.existsById(id)) {
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
    public boolean aumentarInscritos(Long claseId) {
        Optional<claseModel> clase = claseRepository.findById(claseId);
        
        if (clase.isPresent()) {
            claseModel c = clase.get();
            if (c.getInscritos() < c.getCapacidad()) {
                c.setInscritos(c.getInscritos() + 1);
                claseRepository.save(c);
                return true;
            }
        }
        return false;
    }

    // Disminuir inscritos
    public boolean disminuirInscritos(Long claseId) {
        Optional<claseModel> clase = claseRepository.findById(claseId);
        
        if (clase.isPresent()) {
            claseModel c = clase.get();
            if (c.getInscritos() > 0) {
                c.setInscritos(c.getInscritos() - 1);
                claseRepository.save(c);
                return true;
            }
        }
        return false;
    }
}