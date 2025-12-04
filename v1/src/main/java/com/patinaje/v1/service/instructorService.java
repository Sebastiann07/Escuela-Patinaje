package com.patinaje.v1.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patinaje.v1.repository.instructorRepository;
import com.patinaje.v1.model.instructorModel;

@Service
public class instructorService {
    
    @Autowired
    instructorRepository instructorRepository;

    // Obtener todos los instructores
    public List<instructorModel> getAllInstructores() {
        return instructorRepository.findAll();
    }

    // Obtener un instructor por ID
    public Optional<instructorModel> getInstructorById(Long id) {
        return instructorRepository.findById(id);
    }

    // Crear un nuevo instructor
    public instructorModel createInstructor(instructorModel instructor) {
        instructor.setFechaRegistro(LocalDate.now().toString());
        return instructorRepository.save(instructor);
    }

    // Actualizar un instructor
    public instructorModel updateInstructor(Long id, instructorModel instructorDetails) {
        Optional<instructorModel> instructor = instructorRepository.findById(id);
        
        if (instructor.isPresent()) {
            instructorModel existingInstructor = instructor.get();
            
            if (instructorDetails.getNombre() != null) {
                existingInstructor.setNombre(instructorDetails.getNombre());
            }
            if (instructorDetails.getEmail() != null) {
                existingInstructor.setEmail(instructorDetails.getEmail());
            }
            if (instructorDetails.getTelefono() != null) {
                existingInstructor.setTelefono(instructorDetails.getTelefono());
            }
            if (instructorDetails.getEspecialidad() != null) {
                existingInstructor.setEspecialidad(instructorDetails.getEspecialidad());
            }
            if (instructorDetails.getExperiencia() != null) {
                existingInstructor.setExperiencia(instructorDetails.getExperiencia());
            }
            if (instructorDetails.getEstado() != null) {
                existingInstructor.setEstado(instructorDetails.getEstado());
            }
            
            return instructorRepository.save(existingInstructor);
        }
        
        return null;
    }

    // Eliminar un instructor
    public boolean deleteInstructor(Long id) {
        if (instructorRepository.existsById(id)) {
            instructorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Obtener instructor por email
    public Optional<instructorModel> getInstructorByEmail(String email) {
        return instructorRepository.findByEmail(email);
    }

    // Verificar si existe un email
    public boolean emailExists(String email) {
        return instructorRepository.existsByEmail(email);
    }

    // Obtener instructores activos
    public List<instructorModel> getInstructoresActivos() {
        return instructorRepository.findByEstado("Activo");
    }

    // Obtener instructores por especialidad
    public List<instructorModel> getInstructoresByEspecialidad(String especialidad) {
        return instructorRepository.findByEspecialidad(especialidad);
    }
}