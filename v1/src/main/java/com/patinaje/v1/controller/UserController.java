package com.patinaje.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.patinaje.v1.service.userService;
import com.patinaje.v1.model.userModel;

import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    userService userService;

    // HTML - Muestra los usuarios en una página web
    @GetMapping("/listar")
    public String listarHTML(Model model) {
        List<userModel> usuarios = userService.getAllUsers();
        model.addAttribute("users", usuarios);
        model.addAttribute("total", usuarios.size());
        return "users/list";
    }

    // JSON - Para consumir desde aplicaciones (Thunder Client, Postman, etc)
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
}