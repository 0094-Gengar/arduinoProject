package com.example.arduino_project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ESP32Controller {

    private boolean isConnected = false; // 서버 연결 상태 변수

    // 연결 상태를 확인하는 엔드포인트
    @GetMapping("/test")
    public String testConnection() {
        if (isConnected) {
            return "connected!!!";
        } else {
            return "connecting..."; // 연결 중 상태
        }
    }

    // 연결된 상태로 바꾸는 엔드포인트 (예시)
    @GetMapping("/connect")
    public String connect() {
        isConnected = true;
        return "Connected to the server.";
    }

    // 연결 해제 상태로 바꾸는 엔드포인트 (예시)
    @GetMapping("/disconnect")
    public String disconnect() {
        isConnected = false;
        return "Disconnected from the server.";
    }
}
