package com.example.arduino_project.dto;

import com.example.arduino_project.entity.Zone;
import jakarta.persistence.Column;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ZoneDTO {
    public String zoneId;
    public String startTime;  // 순찰 시작 시간을 저장하는 필드
    public String duration;
    public Boolean urgent;
    public Boolean status;

    public ZoneDTO(Zone zone) {
        this.zoneId = formatZoneId(zone.getZoneId());
        this.startTime = formatStartTime(zone.getStartTime());
        this.duration = formatDuration(zone.getStartTime());
        this.urgent = zone.isUrgent();
        this.status = zone.isStatus();
    }

    // startTime을 원하는 형식으로 변환하는 함수
    private String formatStartTime(long ms) {
        if(ms == 0) {
            ms = 0;
        }
        Date date = new Date(ms);  // 밀리초를 Date 객체로 변환
        SimpleDateFormat sdf = new SimpleDateFormat("MM월 dd일 (E) a hh:mm:ss");  // 원하는 형식 정의
        return sdf.format(date);  // 형식에 맞는 문자열 반환
    }

    // 경과 시간을 hh:mm:ss 형식으로 변환하는 함수
    private String formatDuration(long startTimeMs) {
        // 현재 시간을 가져옴
        long utcMillis = System.currentTimeMillis();

        // UTC 밀리초에 KST 오프셋(9시간) 추가
        long currentTimeMs = utcMillis + (9 * 60 * 60 * 1000);

        // 경과 시간 계산
        long durationMs = currentTimeMs - startTimeMs;  // 시작 시간과 현재 시간의 차이

        // 밀리초를 초로 변환
        long seconds = Math.floorDiv(durationMs, 1000);
        long minutes = Math.floorDiv(seconds, 60);
        long hours = Math.floorDiv(minutes, 60);

        // 초, 분, 시간 계산
        seconds = seconds % 60;
        minutes = minutes % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private String formatZoneId(String zoneId) {
        if(zoneId.equals("zone_a")) {
            return "구역 A";
        }
        else if(zoneId.equals("zone_b")) {
            return "구역 B";
        }
        else if(zoneId.equals("zone_c")) {
            return "구역 C";
        }
        return "None";
    }
}
