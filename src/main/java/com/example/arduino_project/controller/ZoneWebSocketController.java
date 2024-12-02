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
        // 구역 상태를 변경하는 로직
        // 예: zoneStatus.put(zoneId, !zoneStatus.get(zoneId));

        // 구역 상태를 클라이언트에게 전송
        messagingTemplate.convertAndSend("/topic/zoneStatus", zoneId);
    }
}
