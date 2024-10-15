plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot 기본 스타터
    implementation("org.springframework.boot:spring-boot-starter")

    // Spring Data JPA for database interaction
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Lombok for reducing boilerplate code
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Spring Web Starter for creating web APIs
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Spring Data JDBC (if you're also using Spring Data JDBC alongside JPA)

    // Spring Boot DevTools for hot reload during development
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Validation for input data validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    runtimeOnly("com.mysql:mysql-connector-j")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Logging dependencies
    implementation("org.slf4j:slf4j-api")
    implementation("ch.qos.logback:logback-classic")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
