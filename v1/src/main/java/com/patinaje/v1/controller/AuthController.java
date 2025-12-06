package com.patinaje.v1.controller;

import com.patinaje.v1.model.userModel;
import com.patinaje.v1.model.instructorModel;
import com.patinaje.v1.service.userService;
import com.patinaje.v1.service.instructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "Auth", description = "Operaciones de autenticación y registro")
public class AuthController {

    @Autowired
    private userService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private instructorService instructorService;


    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       Model model) {
        
        if (error != null) {
            model.addAttribute("error", "Email o contraseña incorrectos");
        }
        
        if (logout != null) {
            model.addAttribute("success", "Has cerrado sesión exitosamente");
        }
        
        return "auth/login";
    }

  
    @GetMapping("/registro")
    public String registroForm(Model model) {
        model.addAttribute("user", new userModel());
        return "auth/registro";
    }

    
    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute("user") userModel user,
                                   BindingResult result,
                                   RedirectAttributes redirect,
                                   Model model,
                                   @RequestParam(name = "tipo", defaultValue = "ALUMNO") String tipo) {
        
       
        if (result.hasErrors()) {
            return "auth/registro";
        }

       
        // Si registra como ALUMNO, validar contra usuarios.
        // Si registra como INSTRUCTOR, validar contra instructores.
        boolean emailOcupado = "INSTRUCTOR".equalsIgnoreCase(tipo)
            ? instructorService.emailExists(user.getEmail())
            : userService.emailExists(user.getEmail());
        if (emailOcupado) {
            model.addAttribute("error", "El email ya está registrado");
            return "auth/registro";
        }

        try {
            if ("INSTRUCTOR".equalsIgnoreCase(tipo)) {
                // Crear instructor
                instructorModel instructor = new instructorModel();
                instructor.setNombre(user.getName());
                instructor.setEmail(user.getEmail());
                instructor.setTelefono(user.getPhone());
                instructor.setEstado("Activo");
                instructor.setPassword(user.getPassword());
                instructorService.createInstructor(instructor);
            } else {
                // Crear alumno (usuario)
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setRol(userModel.Role.ALUMNO);
                user.setActivo(true);
                userService.createUser(user);
            }
            
            redirect.addFlashAttribute("success", "Registro exitoso. Por favor inicia sesión.");
            return "redirect:/login";
            
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar: " + e.getMessage());
            return "auth/registro";
        }
    }

   
    @GetMapping("/dashboard")
    public String dashboard() {
        return "auth/dashboard";
    }
}