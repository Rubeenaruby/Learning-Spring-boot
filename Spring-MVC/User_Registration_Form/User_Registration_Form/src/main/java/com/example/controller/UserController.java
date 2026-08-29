package com.example.controller;

import com.example.entity.User;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration"; // Thymeleaf will look for src/main/resources/templates/registration.html
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "registration"; // Thymeleaf will return to src/main/resources/templates/registration.html
        }

        // Process user registration
        model.addAttribute("user", user); // Add the user to the model to show in result
        return "result"; // Thymeleaf will look for src/main/resources/templates/result.html
    }
    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("commonTitle", "User Registration System");
    }
}