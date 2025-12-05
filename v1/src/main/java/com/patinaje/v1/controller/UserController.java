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

import com.patinaje.v1.service.userService;
import com.patinaje.v1.model.userModel;

import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    userService userService;


    @GetMapping("/listar")
    public String listarHTML(Model model) {
        List<userModel> usuarios = userService.getAllUsers();
        model.addAttribute("users", usuarios);
        model.addAttribute("total", usuarios.size());
        return "users/list";
    }

 
    @GetMapping("/ver/{id}")
    public String verUsuario(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Optional<userModel> usuario = userService.getUserById(id);
        
        if (usuario.isPresent()) {
            model.addAttribute("user", usuario.get());
            return "users/detail";
        } else {
            redirect.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/users/listar";
        }
    }

 
    @GetMapping("/crear")
    public String crearUsuarioForm(Model model) {
        model.addAttribute("user", new userModel());
        return "users/form";
    }

    
    @PostMapping("/guardar")
    public String guardarUsuario(userModel user, RedirectAttributes redirect) {
        try {
          
            if (userService.emailExists(user.getEmail())) {
                redirect.addFlashAttribute("error", "El email ya está registrado");
                return "redirect:/users/crear";
            }
            
            userService.createUser(user);
            redirect.addFlashAttribute("success", "Usuario creado exitosamente");
            return "redirect:/users/listar";
            
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al crear el usuario: " + e.getMessage());
            return "redirect:/users/crear";
        }
    }


    @GetMapping("/editar/{id}")
    public String editarUsuarioForm(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Optional<userModel> usuario = userService.getUserById(id);
        
        if (usuario.isPresent()) {
            model.addAttribute("user", usuario.get());
            return "users/form";
        } else {
            redirect.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/users/listar";
        }
    }

 
    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Long id, userModel userDetails, RedirectAttributes redirect) {
        try {
            Optional<userModel> usuario = userService.getUserById(id);
            
            if (usuario.isPresent()) {
                userModel existingUser = usuario.get();
                
          
                if (userDetails.getName() != null && !userDetails.getName().trim().isEmpty()) {
                    existingUser.setName(userDetails.getName());
                }
                if (userDetails.getEmail() != null && !userDetails.getEmail().trim().isEmpty()) {
                    existingUser.setEmail(userDetails.getEmail());
                }
                if (userDetails.getPassword() != null && !userDetails.getPassword().trim().isEmpty()) {
                    existingUser.setPassword(userDetails.getPassword());
                }
                if (userDetails.getPhone() != null && !userDetails.getPhone().trim().isEmpty()) {
                    existingUser.setPhone(userDetails.getPhone());
                }
                
                userService.createUser(existingUser);
                redirect.addFlashAttribute("success", "Usuario actualizado exitosamente");
                return "redirect:/users/listar";
            } else {
                redirect.addFlashAttribute("error", "Usuario no encontrado");
                return "redirect:/users/listar";
            }
            
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
            return "redirect:/users/editar/" + id;
        }
    }

    // Eliminar usuario
    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            if (userService.deleteUser(id)) {
                redirect.addFlashAttribute("success", "Usuario eliminado exitosamente");
            } else {
                redirect.addFlashAttribute("error", "Usuario no encontrado");
            }
            return "redirect:/users/listar";
            
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
            return "redirect:/users/listar";
        }
    }


    @GetMapping("/api/json")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> listarJSON() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<userModel> usuarios = userService.getAllUsers();
            
            response.put("timestamp", java.time.LocalDateTime.now().toString());
            response.put("status", 200);
            response.put("data", usuarios);
            response.put("total", usuarios.size());
            response.put("path", "/users/api/json");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("timestamp", java.time.LocalDateTime.now().toString());
            response.put("status", 500);
            response.put("error", "Internal Server Error");
            response.put("message", e.getMessage());
            response.put("path", "/users/api/json");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/api/json/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerUsuarioJSON(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<userModel> usuario = userService.getUserById(id);
        
        if (usuario.isPresent()) {
            response.put("status", 200);
            response.put("data", usuario.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("status", 404);
            response.put("error", "Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}