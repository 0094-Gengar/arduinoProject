package com.example.arduino_project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/zones")
public class ZoneController {

    private Map<String, Boolean> zoneStatus = new HashMap<>();

    // 구역 상태 초기화 (예: 구역 A, B, C)
    public ZoneController() {
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
    public ResponseEntity<String> toggleZone(@PathVariable String zoneId) {
        if (!zoneStatus.containsKey(zoneId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid zone");
        }

        boolean currentStatus = zoneStatus.get(zoneId);
        zoneStatus.put(zoneId, !currentStatus); // 상태 반전

        return ResponseEntity.ok("Zone " + zoneId + " updated to " + !currentStatus);
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
