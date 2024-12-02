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

    // 클라이언트로부터 메시지를 받았을 때 구역 상태를 업데이트하고, 실시간으로 다른 클라이언트에게 알림
    @MessageMapping("/zone/toggle")
    public void toggleZone(String zoneId) {
        // 구역 상태를 반전시키는 로직
        boolean newStatus = toggleZoneStatus(zoneId);

        // 구역 상태를 클라이언트에게 전송
        messagingTemplate.convertAndSend("/topic/zoneStatus", zoneId); // zoneId를 클라이언트에 전송
    }

    // 구역 상태 변경 로직 (예시로 상태를 반전시킴)
    private boolean toggleZoneStatus(String zoneId) {
        // 실제 구역 상태를 반전시키는 로직을 구현해야 합니다
        return true; // 예시로 true를 반환
    }
}
