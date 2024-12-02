package com.example.arduino_project.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ZoneWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public ZoneWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/zone/toggle")
    public void toggleZone(String zoneId) {
        boolean newStatus = toggleZoneStatus(zoneId);

        // 구역 상태를 JSON 형태로 전송
        String statusMessage = String.format("{\"zoneId\": \"%s\", \"status\": %b}", zoneId, newStatus);
        messagingTemplate.convertAndSend("/topic/zoneStatus", statusMessage);
    }

    private boolean toggleZoneStatus(String zoneId) {
        // 실제 상태 변경 로직 구현 필요
        // 예시로 상태를 반전시키는 로직을 구현했다고 가정
        return !currentZoneStatus(zoneId); // 기존 상태를 반전
    }

    private boolean currentZoneStatus(String zoneId) {
        // DB 또는 메모리에서 상태를 가져오는 로직
        return false; // 예시로 false 반환
    }
}
