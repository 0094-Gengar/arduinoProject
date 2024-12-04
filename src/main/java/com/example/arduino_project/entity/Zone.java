package com.example.arduino_project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Zone {

    @Id
    private String zoneId;

    private boolean status;

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
}
