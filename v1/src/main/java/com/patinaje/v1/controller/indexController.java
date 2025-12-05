package com.patinaje.v1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping("/index")
@Tag(name = "Index", description = "Vistas públicas principales")
public class indexController {

    @GetMapping("/")
    public String root() {
        return "index"; 
    }

    @GetMapping("/home")
    public String Home() {
        return "index";
    }
}
