package com.example.arduino_project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/zones")
public class ZoneController {

    private final SimpMessagingTemplate messagingTemplate; // SimpMessagingTemplate 인젝션
    private Map<String, Boolean> zoneStatus = new HashMap<>();

    // 구역 상태 초기화 (예: 구역 A, B, C)
    public ZoneController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate; // SimpMessagingTemplate 초기화
        zoneStatus.put("zone_a", false); // 초기 상태는 비활성화
        zoneStatus.put("zone_b", false);
        zoneStatus.put("zone_c", false);
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("zoneStatus", zoneStatus);
        return "home";
    }

    // 구역 활성화/비활성화 상태 변경
    @PostMapping("/{zoneId}/toggle")
    public ResponseEntity<Map<String, Boolean>> toggleZone(@PathVariable String zoneId) {
        boolean newStatus = toggleZoneStatus(zoneId); // 상태 반전

        // WebSocket을 통해 실시간 업데이트 전송
        String statusMessage = String.format("{\"zoneId\": \"%s\", \"status\": %b}", zoneId, newStatus);
        messagingTemplate.convertAndSend("/topic/zoneStatus", statusMessage);

        Map<String, Boolean> response = new HashMap<>();
        response.put("status", newStatus);
        return ResponseEntity.ok(response);
    }

    private boolean toggleZoneStatus(String zoneId) {
        boolean currentStatus = zoneStatus.getOrDefault(zoneId, false); // 현재 상태 가져오기 (기본값: false)
        boolean newStatus = !currentStatus; // 상태 반전
        zoneStatus.put(zoneId, newStatus); // 반전된 상태를 저장
        return newStatus; // 새로운 상태 반환
    }


    // 구역 상태 가져오기
    @GetMapping("/{zoneId}")
    public ResponseEntity<Boolean> getZoneStatus(@PathVariable String zoneId) {
        Boolean status = zoneStatus.get(zoneId);
        if (status == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(status);
    }

    // Getter to access the zoneStatus map for Thymeleaf
    public Map<String, Boolean> getZoneStatus() {
        return zoneStatus;
    }
}
