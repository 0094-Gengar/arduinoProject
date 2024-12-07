package com.example.arduino_project.controller;

import com.example.arduino_project.dto.ZoneDTO;
import com.example.arduino_project.entity.Zone;
import com.example.arduino_project.repository.ZoneRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;  // 트랜잭션 추가
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/zones")
public class ZoneController {

    private final ZoneRepository zoneRepository;

    public ZoneController(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/zones/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<Zone> zones = zoneRepository.findAll();
        List<ZoneDTO> zoneDTOS = new ArrayList<ZoneDTO>();
        for (Zone zone : zones) {
            zoneDTOS.add(new ZoneDTO(zone));
        }
        model.addAttribute("zones", zoneDTOS);
        return "home";
    }

    @PostMapping("/{zoneId}/toggle")
    @ResponseBody
    @Transactional  // 트랜잭션을 명시적으로 추가
    public ResponseEntity<ZoneDTO> toggleZone(@PathVariable String zoneId) {
        return processZoneToggle(zoneId);
    }

    @PostMapping("/{zoneId}/urgent")
    @ResponseBody
    @Transactional  // 트랜잭션을 명시적으로 추가
    public ResponseEntity<HttpStatus> urgent(@PathVariable String zoneId) {
        // /urgent 로 요청이 들어왔을 때
        // 데이터베이스의 상태 바꾸기
        Optional<Zone> optionalZone = zoneRepository.findById(zoneId);
        if (optionalZone.isPresent()) {
            Zone zone = optionalZone.get();
            boolean newUrgent = !zone.isUrgent();
            if (newUrgent) { // 순찰 시작s
                zone.setStartTime(System.currentTimeMillis());
            } else { // 순찰 종료
                zone.setStartTime(0L);
                zone.setEndTime(System.currentTimeMillis());
            }
            zone.setUrgent(newUrgent);
            zoneRepository.save(zone);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 아두아노 태그 시 사용
    private ResponseEntity<ZoneDTO> processZoneToggle(String zoneId) {
        Optional<Zone> optionalZone = zoneRepository.findById(zoneId);
        if (optionalZone.isPresent()) {
            Zone zone = optionalZone.get();
            boolean newStatus = !zone.isStatus();
            zone.setStatus(newStatus);

            // 상태 변경 전에 로그 출력 (디버깅용)
            System.out.println("Before Save: " + zone);

            zone = zoneRepository.save(zone);  // 변경된 구역 상태 저장

            // 상태 변경 후 로그 출력 (디버깅용)
            System.out.println("After Save: " + zone);
            return ResponseEntity.ok(new ZoneDTO(zone));
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

    // 경과 시간만 반환하는 엔드포인트 추가
    @GetMapping("/{zoneId}/duration")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getDuration(@PathVariable String zoneId) {
        Optional<Zone> optionalZone = zoneRepository.findById(zoneId);
        if (optionalZone.isPresent()) {
            Zone zone = optionalZone.get();
        }
        return ResponseEntity.badRequest().build();
    }
}
