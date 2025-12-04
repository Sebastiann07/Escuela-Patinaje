package com.patinaje.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.patinaje.v1.service.userService;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/users")


public class UserController {

    @Autowired
    userService userService;

    @GetMapping("/listar")
    public String listUser (Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users/list";
    }


}

