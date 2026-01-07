## 개발 환경

### 언어 및 프레임워크

- Java 21

- Spring Boot 3.5.9

- Gradle 기반 프로젝트


### 주요 의존성

- Spring Boot Starter Data JPA

- Spring Boot Starter WebMVC

- Spring Boot Starter Validation

- Spring Boot Starter Thymeleaf

- Lombok

- DevTools (개발 편의 기능)

- MySQL Connector-J

- QueryDSL 5.0.0 (Jakarta 버전 사용)

- JSpecify 1.0.0

- Spring AI 1.1.2


### 테스트 의존성

- Spring Boot Starter Data JPA Test

- Spring Boot Starter Thymeleaf Test

- Spring Boot Starter Validation Test

- Spring Boot Starter WebMVC Test

- JUnit Platform Launcher


### QueryDSL 설정

- Q-Class 생성 경로: `src/main/generated/querydsl/`

- `JavaCompile` 시 자동으로 Q-Class가 생성되도록 설정

- `sourceSets`를 통해 Q-Class를 소스 경로에 포함

- `clean` 시 자동 생성된 Q-Class 디렉토리 제거

- QueryDSL 관련 라이브러리는 컴파일 시점에만 사용


### 빌드 및 실행

- `gradlew build` : 프로젝트 빌드

- `gradlew clean` : 생성된 Q-Class 및 빌드 파일 정리

- `gradlew bootRun` : 애플리케이션 실행