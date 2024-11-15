package com.example.arduino_project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ESP01TestController {

    @GetMapping("/update")
    public String receiveSignal(@RequestParam(value = "signal", required = false) String signal) {
        try {
            if (signal == null || signal.isEmpty()) {
                throw new IllegalArgumentException("신호 값이 비어 있습니다.");
            }

            System.out.println("Received Signal: " + signal);
            return "Signal Received";
        } catch (IllegalArgumentException e) {
            // 잘못된 파라미터 예외 처리
            return "연결 실패: " + e.getMessage();
        } catch (Exception e) {
            // 일반 예외 처리
            return "연결 실패: 알 수 없는 오류가 발생했습니다.";
        }
    }
}
