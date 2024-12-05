package com.example.arduino_project.controller;

import com.example.arduino_project.entity.Zone;
import com.example.arduino_project.repository.ZoneRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;  // 트랜잭션 추가
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

    @PostMapping("/{zoneId}/toggle")
    @ResponseBody
    @Transactional  // 트랜잭션을 명시적으로 추가
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

            // 상태 변경 전에 로그 출력 (디버깅용)
            System.out.println("Before Save: " + zone);

            if (newStatus) {
                zone.setStartTime(System.currentTimeMillis());  // 순찰 시작 시간 설정
            } else {
                // 비활성화 시 순찰 경과 시간 계산
                long duration = System.currentTimeMillis() - zone.getStartTime();
                zone.setStartTime(null);  // 순찰 종료되면 시간 초기화

                // duration을 밀리초 단위로 보내기 전에 변환
                String formattedDuration = formatDuration(duration);

                Map<String, String> response = new HashMap<>();
                response.put("zoneId", zoneId);
                response.put("status", "zone_off");
                response.put("duration", formattedDuration);  // 경과 시간 추가 (hh:mm:ss)

                zoneRepository.save(zone);  // 변경된 구역 상태 저장
                return ResponseEntity.ok(response);
            }

            zoneRepository.save(zone);  // 변경된 구역 상태 저장

            // 상태 변경 후 로그 출력 (디버깅용)
            System.out.println("After Save: " + zone);

            Map<String, String> response = new HashMap<>();
            response.put("zoneId", zoneId);
            response.put("status", "zone_on");

            // 활성화 시에는 startTime만 반환
            response.put("startTime", String.valueOf(zone.getStartTime()));

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

    // 경과 시간을 hh:mm:ss 형식으로 변환하는 함수
    private String formatDuration(long ms) {
        long seconds = Math.floorDiv(ms, 1000);
        long minutes = Math.floorDiv(seconds, 60);
        long hours = Math.floorDiv(minutes, 60);
        seconds = seconds % 60;
        minutes = minutes % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    // 경과 시간만 반환하는 엔드포인트 추가
    @GetMapping("/{zoneId}/duration")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getDuration(@PathVariable String zoneId) {
        Optional<Zone> optionalZone = zoneRepository.findById(zoneId);
        if (optionalZone.isPresent()) {
            Zone zone = optionalZone.get();
            if (zone.isStatus() == false && zone.getStartTime() != null) {
                // 비활성화 상태일 때 경과 시간 계산
                long duration = System.currentTimeMillis() - zone.getStartTime();
                String formattedDuration = formatDuration(duration);
                Map<String, String> response = new HashMap<>();
                response.put("zoneId", zoneId);
                response.put("duration", formattedDuration);  // hh:mm:ss 형식으로 경과 시간 반환
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
