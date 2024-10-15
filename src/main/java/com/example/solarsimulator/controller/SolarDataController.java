package com.example.solarsimulator.controller;

import com.example.solarsimulator.service.SolarSimulatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SolarDataController {

    private final SolarSimulatorService solarSimulatorService;

    public SolarDataController(SolarSimulatorService solarSimulatorService) {
        this.solarSimulatorService = solarSimulatorService;
    }

    @GetMapping("/api/solar/generate")
    public String generateSolarData() {
        solarSimulatorService.generateAndSaveSolarData();  // 메서드 이름 변경
        return "Solar data generated!";
    }
}
