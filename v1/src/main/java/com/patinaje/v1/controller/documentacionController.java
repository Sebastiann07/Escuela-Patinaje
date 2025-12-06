package com.patinaje.v1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/documentacion")
public class documentacionController {

    @GetMapping
    public String documentacion() {
        return "documentacion";
    }
}