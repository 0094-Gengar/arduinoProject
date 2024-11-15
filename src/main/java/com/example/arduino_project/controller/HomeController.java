package com.example.arduino_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/testfile")
    public String home() {
        return "testfile";
    }
}
