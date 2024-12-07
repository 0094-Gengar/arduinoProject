package com.example.arduino_project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Zone {

    @Id
    private String zoneId;

    private boolean status;
    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long startTime;  // 순찰 시작 시간을 저장하는 필드
    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long endTime;  // 순찰 종료 시간을 저장하는 필드
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean urgent;

    // 기본 생성자
    public Zone() {
    }

    public Zone(String zoneId, boolean status, Long startTime, Long endTime, boolean urgent) {
        this.zoneId = zoneId;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.urgent = urgent;
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

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }
}
