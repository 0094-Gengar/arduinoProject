package com.example.arduino_project.repository;

import com.example.arduino_project.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<Zone, String> {
}
