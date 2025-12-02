package com.patinaje.v1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller

@RequestMapping("/index")
public class indexController {
    @GetMapping("/home")
    public String Home()
    {
        return "index";
    }
    

}
