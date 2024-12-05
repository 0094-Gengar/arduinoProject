package com.example.arduino_project.controller;

import com.example.arduino_project.entity.Zone;
import com.example.arduino_project.repository.ZoneRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/zones")
public class ZoneController {

    private final ZoneRepository zoneRepository;

    public ZoneController(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;

        // 초기 상태 설정: 존재하지 않으면 기본 구역 생성
        if (!zoneRepository.existsById("zone_a")) {
            zoneRepository.save(new Zone("zone_a", false));
            zoneRepository.save(new Zone("zone_b", false));
            zoneRepository.save(new Zone("zone_c", false));
        }
    }

    // /zones/home으로 리다이렉트 추가
    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/zones/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        Map<String, Boolean> zoneStatus = new HashMap<>();
        for (Zone zone : zoneRepository.findAll()) {
            zoneStatus.put(zone.getZoneId(), zone.isStatus());
        }
        model.addAttribute("zoneStatus", zoneStatus);
        return "home";
    }

    // 버튼 클릭 시: 기존 로직 유지 (뷰와 관련된 요청 처리)
    @PostMapping("/{zoneId}/toggle")
    @ResponseBody
    public ResponseEntity<Map<String, String>> toggleZone(@PathVariable String zoneId) {
        return processZoneToggle(zoneId);
    }

    // 공통 처리 로직 분리
    private ResponseEntity<Map<String, String>> processZoneToggle(String zoneId) {
        Optional<Zone> optionalZone = zoneRepository.findById(zoneId);
        if (optionalZone.isPresent()) {
            Zone zone = optionalZone.get();
            boolean newStatus = !zone.isStatus();
            zone.setStatus(newStatus);

            if (newStatus) {
                zone.setStartTime(System.currentTimeMillis());  // 순찰 시작 시간 설정
            } else {
                zone.setStartTime(null);  // 순찰이 종료되면 시간 초기화 (선택 사항)
            }

            zoneRepository.save(zone);

            Map<String, String> response = new HashMap<>();
            response.put("zoneId", zoneId);
            response.put("status", newStatus ? "zone_on" : "zone_off");

            if (newStatus) {
                response.put("startTime", String.valueOf(zone.getStartTime()));  // startTime만 반환
            }

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{zoneId}")
    @ResponseBody
    public ResponseEntity<Boolean> getZoneStatus(@PathVariable String zoneId) {
        return zoneRepository.findById(zoneId)
                .map(zone -> ResponseEntity.ok(zone.isStatus()))
                .orElse(ResponseEntity.badRequest().build());
    }
}
