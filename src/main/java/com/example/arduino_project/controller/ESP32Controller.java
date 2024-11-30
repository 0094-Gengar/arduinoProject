package com.example.arduino_project.controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class ESP32Controller {

    private boolean isConnected = false;

    @GetMapping("/")
    public String home() {
        return "Welcome to Arduino Project!";
    }

    @GetMapping("/test")
    public String testConnection() {
        return isConnected ? "connected!!!" : "connecting...";
    }

    // POST 요청을 처리하여 연결 상태 변경
    @PostMapping("/connect")
    public String connect() {
        isConnected = true;
        return "ESP32 connected!";
    }

    @PostMapping("/disconnect")
    public String disconnect() {
        isConnected = false;
        return "ESP32 disconnected!";
    }
}
