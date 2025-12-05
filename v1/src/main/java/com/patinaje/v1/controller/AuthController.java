package com.patinaje.v1.controller;

import com.patinaje.v1.model.userModel;
import com.patinaje.v1.service.userService;
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

@Controller
public class AuthController {

    @Autowired
    private userService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Página de login
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

    // Página de registro público
    @GetMapping("/registro")
    public String registroForm(Model model) {
        model.addAttribute("user", new userModel());
        return "auth/registro";
    }

    // Procesar registro
    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute("user") userModel user,
                                   BindingResult result,
                                   RedirectAttributes redirect,
                                   Model model) {
        
        // Validar errores de formulario
        if (result.hasErrors()) {
            return "auth/registro";
        }

        // Verificar si el email ya existe
        if (userService.emailExists(user.getEmail())) {
            model.addAttribute("error", "El email ya está registrado");
            return "auth/registro";
        }

        try {
            // Encriptar la contraseña
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            
            // Asignar rol de ALUMNO por defecto
            user.setRol(userModel.Role.ALUMNO);
            user.setActivo(true);
            
            // Guardar usuario
            userService.createUser(user);
            
            redirect.addFlashAttribute("success", "Registro exitoso. Por favor inicia sesión.");
            return "redirect:/login";
            
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar: " + e.getMessage());
            return "auth/registro";
        }
    }

    // Dashboard después del login
    @GetMapping("/dashboard")
    public String dashboard() {
        return "auth/dashboard";
    }
}