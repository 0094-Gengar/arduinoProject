package com.example.arduino_project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/zones")
public class ZoneController {

    private final Map<String, Boolean> zoneStatus = new HashMap<>();

    public ZoneController() {
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
        boolean newStatus = !zoneStatus.getOrDefault(zoneId, false);
        zoneStatus.put(zoneId, newStatus);

        Map<String, String> response = new HashMap<>();
        response.put("zoneId", zoneId);
        response.put("status", newStatus ? "zone_on" : "zone_off");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{zoneId}")
    public ResponseEntity<Boolean> getZoneStatus(@PathVariable String zoneId) {
        Boolean status = zoneStatus.get(zoneId);
        return status != null
                ? ResponseEntity.ok(status)
                : ResponseEntity.badRequest().build();
    }
}
