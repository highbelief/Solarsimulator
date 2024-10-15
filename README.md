# 태양광 발전소 시뮬레이터

이 프로젝트는 대학 프로젝트의 일환으로 개발된 태양광 발전소 시뮬레이터입니다. 실제 태양광 발전 시스템을 시뮬레이션하여 실시간으로 발전량과 기상 데이터를 생성합니다. 이 시뮬레이터는 **교육 목적으로만** 사용해야 하며, 상업적 또는 실무 환경에서는 사용하지 마십시오.

## 설명

이 시뮬레이터는 1초마다 기상 및 태양광 발전 데이터를 생성하여 MySQL 데이터베이스에 저장합니다. 시뮬레이션은 시간대, 구름량 등 기상 조건에 따라 발전량을 계산합니다.

### 주요 기능

- 실시간 기상 데이터 생성 (온도, 일사량, 구름량, 풍속 등).
- 태양광 발전 시뮬레이션, 배터리 상태 및 장비 상태 모니터링.
- MySQL 데이터베이스에 데이터 저장.
- 확장 가능하고 모듈화된 Spring Boot 프로젝트 구조.

## 사용 지침

- **교육 목적으로만 사용**: 이 프로젝트는 대학 프로젝트의 일환으로 제작되었으며, 학습 및 교육 시연 목적으로만 사용해야 합니다.
- **성능 보장 없음**: 이 프로그램은 어떠한 성능 보장도 하지 않으며, 상용 또는 실무 환경에서 사용해서는 안 됩니다.

## 빌드 환경

다음은 시뮬레이터를 빌드하는 데 사용된 주요 기술과 도구입니다:

- **Java 21**: 애플리케이션의 주요 프로그래밍 언어.
- **Spring Boot 3.3.4**: 모듈화되고 확장 가능한 구조를 제공하는 프레임워크.
- **MySQL**: 기상 데이터 및 발전 데이터를 저장하는 데이터베이스.
- **Maven 또는 Gradle**: 의존성 관리 및 프로젝트 빌드 도구.

### 의존성

다음은 프로젝트에서 사용된 주요 의존성입니다:
- Spring Boot Starter Data JPA
- Spring Boot Starter Web
- MySQL Connector
- Lombok (보일러플레이트 코드를 줄이기 위해 사용)

## 설치 및 설정 방법

1. 리포지토리 클론:

```bash
git clone <repository-url>
cd solarsimulator
```
2. MySQL :
```bash
-- SolarDB 데이터베이스 생성
CREATE DATABASE SolarDB;

-- SolarDB 선택
USE SolarDB;

-- 선택사항: 데이터베이스 삭제
-- DROP DATABASE SolarDB;

-- 사용자 생성 및 권한 부여
CREATE USER 'solar_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON SolarDB.* TO 'solar_user'@'localhost';
FLUSH PRIVILEGES;

-- weather_data 테이블 생성
CREATE TABLE weather_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    observation_time TIMESTAMP NOT NULL,  -- 기상 데이터 관측 시간
    temperature DOUBLE,                   -- 기온 (°C)
    solar_irradiance DOUBLE,              -- 일사량 (W/m²)
    cloud_coverage DOUBLE,                -- 구름량 (0.0 ~ 1.0)
    wind_speed DOUBLE,                    -- 풍속 (m/s)
    precipitation DOUBLE,                 -- 강수량 (mm)
    humidity DOUBLE,                      -- 습도 (%)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- solar_data 테이블 생성
CREATE TABLE solar_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    observation_time TIMESTAMP NOT NULL,  -- 발전 데이터 관측 시간
    power_generated DOUBLE,               -- 발전량 (kW 또는 MW)
    battery_level DOUBLE,                 -- 배터리 상태 (%)
    equipment_status VARCHAR(255),        -- 장비 상태 (정상, 고장 등)
    inverter_status VARCHAR(255),         -- 인버터 상태 (정상, 고장 등)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```
