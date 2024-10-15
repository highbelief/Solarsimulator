package com.example.solarsimulator;

import com.example.solarsimulator.model.WeatherData;
import com.example.solarsimulator.repository.WeatherDataRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.util.Random;

@SpringBootApplication
@EnableScheduling  // 스케줄링 활성화
public class SolarsimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SolarsimulatorApplication.class, args);
    }

    // 애플리케이션 시작 시 초기 기상 데이터를 추가하는 메서드
    @Bean
    public ApplicationRunner dataLoader(WeatherDataRepository weatherDataRepository) {
        return args -> {
            if (weatherDataRepository.count() == 0) {
                WeatherData weatherData = new WeatherData();
                Random random = new Random();

                weatherData.setObservationTime(LocalDateTime.now());
                weatherData.setTemperature(random.nextDouble() * 40);
                weatherData.setSolarIrradiance(random.nextDouble() * 1000);
                weatherData.setCloudCoverage(random.nextDouble());
                weatherData.setWindSpeed(random.nextDouble() * 20);
                weatherData.setPrecipitation(random.nextDouble() * 10);
                weatherData.setHumidity(random.nextDouble() * 100);

                weatherDataRepository.save(weatherData);
            }
        };
    }
}
