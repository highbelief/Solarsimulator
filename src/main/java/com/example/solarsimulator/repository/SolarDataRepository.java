package com.example.solarsimulator.repository;

import com.example.solarsimulator.model.SolarData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolarDataRepository extends JpaRepository<SolarData, Long> {
}
