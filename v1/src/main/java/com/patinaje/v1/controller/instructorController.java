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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.patinaje.v1.service.instructorService;
import com.patinaje.v1.model.instructorModel;

import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/instructores")
@Tag(name = "Instructores", description = "Operaciones sobre instructores")
public class instructorController {

    @Autowired
    instructorService instructorService;


    @GetMapping("/listar")
    public String listarInstructores(Model model) {
        List<instructorModel> instructores = instructorService.getAllInstructores();
        model.addAttribute("instructores", instructores);
        model.addAttribute("total", instructores.size());
        return "instructores/list";
    }


    @GetMapping("/ver/{id}")
    public String verInstructor(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Optional<instructorModel> instructor = instructorService.getInstructorById(id);
        
        if (instructor.isPresent()) {
            model.addAttribute("instructor", instructor.get());
            return "instructores/detail";
        } else {
            redirect.addFlashAttribute("error", "Instructor no encontrado");
            return "redirect:/instructores/listar";
        }
    }


    @GetMapping("/crear")
    public String crearInstructorForm(Model model) {
        model.addAttribute("instructor", new instructorModel());
        return "instructores/form";
    }


    @PostMapping("/guardar")
    public String guardarInstructor(instructorModel instructor, RedirectAttributes redirect) {
        try {
       
            if (instructorService.emailExists(instructor.getEmail())) {
                redirect.addFlashAttribute("error", "El email ya está registrado");
                return "redirect:/instructores/crear";
            }
            
            instructorService.createInstructor(instructor);
            redirect.addFlashAttribute("success", "Instructor creado exitosamente");
            return "redirect:/instructores/listar";
            
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al crear el instructor: " + e.getMessage());
            return "redirect:/instructores/crear";
        }
    }

   
    @GetMapping("/editar/{id}")
    public String editarInstructorForm(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Optional<instructorModel> instructor = instructorService.getInstructorById(id);
        
        if (instructor.isPresent()) {
            model.addAttribute("instructor", instructor.get());
            return "instructores/form";
        } else {
            redirect.addFlashAttribute("error", "Instructor no encontrado");
            return "redirect:/instructores/listar";
        }
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarInstructor(@PathVariable Long id, instructorModel instructorDetails, RedirectAttributes redirect) {
        try {
            instructorModel instructorActualizado = instructorService.updateInstructor(id, instructorDetails);
            
            if (instructorActualizado != null) {
                redirect.addFlashAttribute("success", "Instructor actualizado exitosamente");
                return "redirect:/instructores/listar";
            } else {
                redirect.addFlashAttribute("error", "Instructor no encontrado");
                return "redirect:/instructores/listar";
            }
            
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
            return "redirect:/instructores/editar/" + id;
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarInstructor(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            if (instructorService.deleteInstructor(id)) {
                redirect.addFlashAttribute("success", "Instructor eliminado exitosamente");
            } else {
                redirect.addFlashAttribute("error", "Instructor no encontrado");
            }
            return "redirect:/instructores/listar";
            
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
            return "redirect:/instructores/listar";
        }
    }



    @GetMapping("/api/json")
    @ResponseBody
    @Operation(summary = "Listar instructores (JSON)", description = "Devuelve la lista de instructores en JSON")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listado obtenido"),
        @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public ResponseEntity<Map<String, Object>> listarInstructoresJSON() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<instructorModel> instructores = instructorService.getAllInstructores();
            
            response.put("timestamp", java.time.LocalDateTime.now().toString());
            response.put("status", 200);
            response.put("data", instructores);
            response.put("total", instructores.size());
            response.put("path", "/instructores/api/json");
            
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
    @Operation(summary = "Obtener instructor por ID (JSON)", description = "Devuelve un instructor por su id en JSON")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Instructor encontrado"),
        @ApiResponse(responseCode = "404", description = "Instructor no encontrado")
    })
    public ResponseEntity<Map<String, Object>> obtenerInstructorJSON(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<instructorModel> instructor = instructorService.getInstructorById(id);
        
        if (instructor.isPresent()) {
            response.put("status", 200);
            response.put("data", instructor.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("status", 404);
            response.put("error", "Instructor no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}