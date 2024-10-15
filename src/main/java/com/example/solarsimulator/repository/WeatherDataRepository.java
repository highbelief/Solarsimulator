package com.example.solarsimulator.repository;

import com.example.solarsimulator.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    // 가장 최근의 관측 시간을 가진 데이터를 찾는 메서드
    Optional<WeatherData> findTopByOrderByObservationTimeDesc();
}
