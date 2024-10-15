package com.example.solarsimulator.service;

import com.example.solarsimulator.model.WeatherData;
import com.example.solarsimulator.repository.WeatherDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class WeatherSimulatorService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherSimulatorService.class);
    private final WeatherDataRepository weatherDataRepository;
    private final Random random = new Random();

    public WeatherSimulatorService(WeatherDataRepository weatherDataRepository) {
        this.weatherDataRepository = weatherDataRepository;
    }

    // 1초마다 기상 데이터 생성 및 저장
    @Scheduled(fixedRate = 1000)  // 1000ms = 1초
    public void generateAndSaveWeatherData() {
        WeatherData weatherData = new WeatherData();

        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int month = now.getMonthValue();

        // 시간대와 계절을 반영한 기온 설정
        double temperature = generateTemperature(hour, month);

        // 구름량 설정
        double cloudCoverage = random.nextDouble();  // 0.0 ~ 1.0

        // 일사량은 구름량에 반비례하여 설정
        double solarIrradiance = generateSolarIrradiance(hour, cloudCoverage);

        // 풍속과 강수량 설정
        double windSpeed = generateWindSpeed(hour, month);
        double precipitation = generatePrecipitation(cloudCoverage);

        // 데이터 저장
        weatherData.setObservationTime(now);
        weatherData.setTemperature(temperature);
        weatherData.setSolarIrradiance(solarIrradiance);
        weatherData.setCloudCoverage(cloudCoverage);
        weatherData.setWindSpeed(windSpeed);
        weatherData.setPrecipitation(precipitation);
        weatherData.setHumidity(random.nextDouble() * 100);  // 습도는 0 ~ 100%

        weatherDataRepository.save(weatherData);

        // 로그 기록
        logger.info("Generated weather data and saved to DB: {}", weatherData);
    }

    // 시간대와 계절에 따른 기온 생성
    private double generateTemperature(int hour, int month) {
        double baseTemperature;

        // 계절별 기본 기온 설정 (예: 여름에 기온이 더 높음)
        if (month >= 6 && month <= 8) {
            baseTemperature = 25.0;  // 여름 기본 기온
        } else if (month == 12 || month == 1 || month == 2) {
            baseTemperature = 5.0;  // 겨울 기본 기온
        } else {
            baseTemperature = 15.0;  // 봄/가을 기본 기온
        }

        // 시간대에 따라 기온 변동 (아침/저녁은 더 낮고, 낮은 더 높음)
        if (hour >= 6 && hour < 12) {
            return baseTemperature + random.nextDouble() * 5;  // 아침
        } else if (hour >= 12 && hour < 18) {
            return baseTemperature + random.nextDouble() * 10 + 5;  // 낮
        } else {
            return baseTemperature + random.nextDouble() * 5 - 5;  // 저녁/밤
        }
    }

    // 시간대 및 구름량에 따른 일사량 생성
    private double generateSolarIrradiance(int hour, double cloudCoverage) {
        if (hour >= 6 && hour <= 18) {
            double baseSolarIrradiance = 1000 * (1 - cloudCoverage);  // 구름이 많을수록 일사량 감소
            return baseSolarIrradiance * (0.7 + random.nextDouble() * 0.3);  // 약간의 랜덤 요소 추가
        } else {
            return 0;  // 밤에는 일사량 없음
        }
    }

    // 시간대와 계절에 따른 풍속 생성
    private double generateWindSpeed(int hour, int month) {
        double baseWindSpeed = random.nextDouble() * 10;  // 기본 풍속

        // 시간대에 따라 풍속 변화 (낮/저녁이 바람이 더 셈)
        if (hour >= 12 && hour <= 18) {
            baseWindSpeed += 5;
        }

        // 겨울에는 바람이 더 강함
        if (month == 12 || month == 1 || month == 2) {
            baseWindSpeed += 3;
        }

        return baseWindSpeed;
    }

    // 구름량에 따른 강수량 생성
    private double generatePrecipitation(double cloudCoverage) {
        if (cloudCoverage > 0.8) {
            return random.nextDouble() * 10;  // 구름이 많을수록 비가 올 확률 증가
        } else {
            return 0;  // 구름이 적으면 비 없음
        }
    }
}
