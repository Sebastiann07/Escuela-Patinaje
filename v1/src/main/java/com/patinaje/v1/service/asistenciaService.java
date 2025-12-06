package com.patinaje.v1.service;

import com.patinaje.v1.model.asistenciaModel;
import com.patinaje.v1.model.userModel;
import com.patinaje.v1.model.claseModel;
import com.patinaje.v1.repository.asistenciaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class asistenciaService {

    private final asistenciaRepository repository;

    public asistenciaService(asistenciaRepository repository) {
        this.repository = repository;
    }

    public void guardar(asistenciaModel asistencia) {
        repository.save(asistencia);
    }

    public List<asistenciaModel> getAll() {
        return repository.findAll();
    }

    public List<asistenciaModel> obtenerPorAlumno(userModel alumno) {
        return repository.findByAlumno(alumno);
    }

    public List<asistenciaModel> obtenerPorClase(claseModel clase) {
        return repository.findByClase(clase);
    }
}
