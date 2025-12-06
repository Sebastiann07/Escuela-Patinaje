package com.patinaje.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "Clases", description = "Operaciones sobre las clases")
public class claseController {

    @Autowired
    private claseService claseService;

    @Autowired
    private instructorService instructorService;

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
    public String crearClaseForm(Model model, org.springframework.security.core.Authentication auth) {
        claseModel clase = new claseModel();
        // Si el autenticado es INSTRUCTOR, precargar su usuario como instructor de la clase
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("INSTRUCTOR"))) {
            String email = auth.getName();
            instructorService.getInstructorByEmail(email).ifPresent(clase::setInstructor);
        }
        model.addAttribute("clase", clase);
        List<instructorModel> instructores = instructorService.getInstructoresActivos();
        model.addAttribute("instructores", instructores);
        return "clases/form";
    }

    // Guardar nueva clase
    @PostMapping("/guardar")
    public String guardarClase(@ModelAttribute claseModel clase, RedirectAttributes redirect) {
        try {
            // Validación básica
            if (clase.getNombre() == null || clase.getNombre().trim().isEmpty()) {
                redirect.addFlashAttribute("error", "El nombre de la clase es obligatorio");
                return "redirect:/clases/crear";
            }
            
            if (clase.getCapacidad() == null || clase.getCapacidad() < 1) {
                redirect.addFlashAttribute("error", "La capacidad debe ser mayor a 0");
                return "redirect:/clases/crear";
            }
            
            claseService.createClase(clase);
            redirect.addFlashAttribute("success", "Clase creada exitosamente");
            return "redirect:/clases/listar";
            
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/clases/crear";
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
    public String actualizarClase(@PathVariable Long id, @ModelAttribute claseModel claseDetails, RedirectAttributes redirect) {
        try {
            claseModel claseActualizada = claseService.updateClase(id, claseDetails);
            
            if (claseActualizada != null) {
                redirect.addFlashAttribute("success", "Clase actualizada exitosamente");
                return "redirect:/clases/listar";
            } else {
                redirect.addFlashAttribute("error", "Clase no encontrada");
                return "redirect:/clases/listar";
            }
            
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/clases/editar/" + id;
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
            
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/clases/listar";
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
            return "redirect:/clases/listar";
        }
    }

    // Inscribir alumno a una clase (incrementa inscritos si hay cupo)
    @PostMapping("/inscribir/{id}")
    public String inscribirEnClase(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            if (!claseService.tieneDisponibilidad(id)) {
                redirect.addFlashAttribute("error", "La clase está llena, no hay cupos disponibles");
                return "redirect:/clases/listar";
            }
            claseService.aumentarInscritos(id);
            redirect.addFlashAttribute("success", "Inscripción realizada correctamente");
            return "redirect:/clases/listar";
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/clases/listar";
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al inscribirse: " + e.getMessage());
            return "redirect:/clases/listar";
        }
    }

    // Cancelar inscripción (decrementa inscritos si hay alguno)
    @PostMapping("/cancelar/{id}")
    public String cancelarInscripcion(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            claseService.disminuirInscritos(id);
            redirect.addFlashAttribute("success", "Inscripción cancelada correctamente");
            return "redirect:/clases/listar";
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/clases/listar";
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al cancelar inscripción: " + e.getMessage());
            return "redirect:/clases/listar";
        }
    }

    // ===== APIs JSON =====

    @GetMapping("/api/json")
    @ResponseBody
    @Operation(summary = "Listar clases (JSON)", description = "Devuelve todas las clases en formato JSON")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de clases"),
        @ApiResponse(responseCode = "500", description = "Error interno")
    })
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
    @Operation(summary = "Obtener clase por ID (JSON)", description = "Devuelve una clase por su id en formato JSON")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clase encontrada"),
        @ApiResponse(responseCode = "404", description = "Clase no encontrada")
    })
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