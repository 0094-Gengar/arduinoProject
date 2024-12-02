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

    private final SimpMessagingTemplate messagingTemplate;
    private Map<String, Boolean> zoneStatus = new HashMap<>();

    public ZoneController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        zoneStatus.put("zone_a", false);
        zoneStatus.put("zone_b", false);
        zoneStatus.put("zone_c", false);
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("zoneStatus", zoneStatus);
        return "home";
    }

    @PostMapping("/{zoneId}/toggle")
    public ResponseEntity<Map<String, String>> toggleZone(@PathVariable String zoneId) {
        boolean newStatus = toggleZoneStatus(zoneId);

        // 'true'일 때는 'zone_on', 'false'일 때는 'zone_off'로 변환
        String status = newStatus ? "zone_on" : "zone_off";

        // WebSocket을 통해 실시간 업데이트 전송
        String statusMessage = String.format("{\"zoneId\": \"%s\", \"status\": \"%s\"}", zoneId, status);
        messagingTemplate.convertAndSend("/topic/zoneStatus", statusMessage);

        Map<String, String> response = new HashMap<>();
        response.put("status", status);  // 응답을 'zone_on' 또는 'zone_off'로 설정

        return ResponseEntity.ok(response);
    }

    private boolean toggleZoneStatus(String zoneId) {
        boolean currentStatus = zoneStatus.getOrDefault(zoneId, false);
        boolean newStatus = !currentStatus;
        zoneStatus.put(zoneId, newStatus);
        return newStatus;
    }

    @GetMapping("/{zoneId}")
    public ResponseEntity<Boolean> getZoneStatus(@PathVariable String zoneId) {
        Boolean status = zoneStatus.get(zoneId);
        if (status == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(status);
    }

    public Map<String, Boolean> getZoneStatus() {
        return zoneStatus;
    }
}
