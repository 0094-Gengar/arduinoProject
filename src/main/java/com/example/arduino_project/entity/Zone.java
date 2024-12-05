package com.example.arduino_project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Zone {

    @Id
    private String zoneId;

    private boolean status;
    private Long startTime;  // 순찰 시작 시간을 저장하는 필드

    // 기본 생성자
    public Zone() {}

    public Zone(String zoneId, boolean status) {
        this.zoneId = zoneId;
        this.status = status;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
}
