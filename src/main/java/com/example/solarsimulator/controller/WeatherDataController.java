package com.example.solarsimulator.controller;

import com.example.solarsimulator.service.WeatherSimulatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherDataController {

    private final WeatherSimulatorService weatherSimulatorService;

    public WeatherDataController(WeatherSimulatorService weatherSimulatorService) {
        this.weatherSimulatorService = weatherSimulatorService;
    }

    @GetMapping("/api/weather/generate")
    public String generateWeatherData() {
        weatherSimulatorService.generateAndSaveWeatherData();
        return "Weather data generated!";
    }
}
