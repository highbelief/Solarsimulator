package com.example.solarsimulator.service;

import com.example.solarsimulator.model.SolarData;
import com.example.solarsimulator.model.WeatherData;
import com.example.solarsimulator.repository.SolarDataRepository;
import com.example.solarsimulator.repository.WeatherDataRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class SolarSimulatorService {

    private static final Logger logger = LoggerFactory.getLogger(SolarSimulatorService.class);
    private final SolarDataRepository solarDataRepository;
    private final WeatherDataRepository weatherDataRepository;
    private final Random random = new Random();

    public SolarSimulatorService(SolarDataRepository solarDataRepository, WeatherDataRepository weatherDataRepository) {
        this.solarDataRepository = solarDataRepository;
        this.weatherDataRepository = weatherDataRepository;
    }

    // 트랜잭션 관리 및 1초마다 데이터 생성
    @Transactional  // 트랜잭션 관리
    @Scheduled(fixedRate = 1000)  // 1000ms = 1초마다 실행
    public void generateAndSaveSolarData() {
        logger.debug("generateAndSaveSolarData() is being executed");
        // 최근 기상 데이터 가져오기
        Optional<WeatherData> weatherDataOpt = weatherDataRepository.findTopByOrderByObservationTimeDesc();

        if (weatherDataOpt.isPresent()) {
            WeatherData weatherData = weatherDataOpt.get();
            SolarData solarData = new SolarData();

            // 발전량을 시간대 및 구름량에 따라 조정
            double powerGenerated = calculatePowerBasedOnTimeAndWeather(weatherData);

            solarData.setObservationTime(LocalDateTime.now());
            solarData.setPowerGenerated(powerGenerated);

            // 배터리 상태 업데이트
            double batteryLevel = calculateBatteryLevel(powerGenerated);
            solarData.setBatteryLevel(batteryLevel);

            // 장비 및 인버터 상태
            solarData.setEquipmentStatus(random.nextDouble() > 0.9 ? "고장" : "정상");
            solarData.setInverterStatus(random.nextDouble() > 0.95 ? "고장" : "정상");

            // 데이터 저장
            solarDataRepository.save(solarData);

            // 로그 기록
            logger.info("Generated solar data and saved to DB: Power Generated = {}, Battery Level = {}, Equipment Status = {}, Inverter Status = {}",
                    solarData.getPowerGenerated(),
                    solarData.getBatteryLevel(),
                    solarData.getEquipmentStatus(),
                    solarData.getInverterStatus());
        } else {
            logger.warn("No weather data found to generate solar data.");
        }
    }

    // 시간대와 기상 조건에 따른 발전량 계산 (기존에 정의한 메서드)
    private double calculatePowerBasedOnTimeAndWeather(WeatherData weatherData) {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        double basePower;

        // 시간대별 발전량 설정
        if (hour >= 6 && hour < 10) {
            basePower = random.nextDouble() * 30 + 20;  // 아침 (20 ~ 50 kW)
        } else if (hour >= 10 && hour < 16) {
            basePower = random.nextDouble() * 50 + 70;  // 낮 (70 ~ 120 kW)
        } else if (hour >= 16 && hour < 18) {
            basePower = random.nextDouble() * 30 + 20;  // 저녁 (20 ~ 50 kW)
        } else {
            basePower = 0;  // 밤 (발전량 0)
        }

        // 구름량에 따른 발전량 감소
        double cloudCoverage = weatherData.getCloudCoverage();
        return basePower * (1 - cloudCoverage);  // 구름이 많을수록 발전량 감소
    }

    // 발전량에 따른 배터리 충전 상태 계산 (기존에 정의한 메서드)
    private double calculateBatteryLevel(double powerGenerated) {
        double currentBatteryLevel = random.nextDouble() * 50;
        double newBatteryLevel = currentBatteryLevel + powerGenerated * 0.05;
        return Math.min(newBatteryLevel, 100.0);  // 배터리 상태는 100%를 넘지 않도록 제한
    }
}
