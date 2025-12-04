package com.patinaje.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.patinaje.v1.service.claseService;
import com.patinaje.v1.service.instructorService;
import com.patinaje.v1.model.claseModel;
import com.patinaje.v1.model.instructorModel;

import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/clases")
public class claseController {

    @Autowired
    claseService claseService;

    @Autowired
    instructorService instructorService;

    // ===== VISTAS HTML =====

    // Listar todas las clases
    @GetMapping("/listar")
    public String listarClases(Model model) {
        List<claseModel> clases = claseService.getAllClases();
        List<instructorModel> instructores = instructorService.getAllInstructores();
        
        model.addAttribute("clases", clases);
        model.addAttribute("instructores", instructores);
        model.addAttribute("total", clases.size());
        return "clases/list";
    }

    // Ver detalles de una clase
    @GetMapping("/ver/{id}")
    public String verClase(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Optional<claseModel> clase = claseService.getClaseById(id);
        
        if (clase.isPresent()) {
            model.addAttribute("clase", clase.get());
            return "clases/detail";
        } else {
            redirect.addFlashAttribute("error", "Clase no encontrada");
            return "redirect:/clases/listar";
        }
    }

    // Formulario para crear nueva clase
    @GetMapping("/crear")
    public String crearClaseForm(Model model) {
        model.addAttribute("clase", new claseModel());
        List<instructorModel> instructores = instructorService.getInstructoresActivos();
        model.addAttribute("instructores", instructores);
        return "clases/form";
    }

    // Guardar nueva clase
    @PostMapping("/guardar")
    public String guardarClase(claseModel clase, RedirectAttributes redirect) {
        try {
            claseService.createClase(clase);
            redirect.addFlashAttribute("success", "Clase creada exitosamente");
            return "redirect:/clases/listar";
            
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al crear la clase: " + e.getMessage());
            return "redirect:/clases/crear";
        }
    }

    // Formulario para editar clase
    @GetMapping("/editar/{id}")
    public String editarClaseForm(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Optional<claseModel> clase = claseService.getClaseById(id);
        
        if (clase.isPresent()) {
            model.addAttribute("clase", clase.get());
            List<instructorModel> instructores = instructorService.getAllInstructores();
            model.addAttribute("instructores", instructores);
            return "clases/form";
        } else {
            redirect.addFlashAttribute("error", "Clase no encontrada");
            return "redirect:/clases/listar";
        }
    }

    // Actualizar clase
    @PostMapping("/actualizar/{id}")
    public String actualizarClase(@PathVariable Long id, claseModel claseDetails, RedirectAttributes redirect) {
        try {
            claseModel claseActualizada = claseService.updateClase(id, claseDetails);
            
            if (claseActualizada != null) {
                redirect.addFlashAttribute("success", "Clase actualizada exitosamente");
                return "redirect:/clases/listar";
            } else {
                redirect.addFlashAttribute("error", "Clase no encontrada");
                return "redirect:/clases/listar";
            }
            
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
            return "redirect:/clases/editar/" + id;
        }
    }

    // Eliminar clase
    @PostMapping("/eliminar/{id}")
    public String eliminarClase(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            if (claseService.deleteClase(id)) {
                redirect.addFlashAttribute("success", "Clase eliminada exitosamente");
            } else {
                redirect.addFlashAttribute("error", "Clase no encontrada");
            }
            return "redirect:/clases/listar";
            
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
            return "redirect:/clases/listar";
        }
    }

    // ===== APIs JSON =====

    @GetMapping("/api/json")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> listarClasesJSON() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<claseModel> clases = claseService.getAllClases();
            
            response.put("timestamp", java.time.LocalDateTime.now().toString());
            response.put("status", 200);
            response.put("data", clases);
            response.put("total", clases.size());
            response.put("path", "/clases/api/json");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("timestamp", java.time.LocalDateTime.now().toString());
            response.put("status", 500);
            response.put("error", "Internal Server Error");
            response.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/api/json/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerClaseJSON(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<claseModel> clase = claseService.getClaseById(id);
        
        if (clase.isPresent()) {
            response.put("status", 200);
            response.put("data", clase.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("status", 404);
            response.put("error", "Clase no encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}