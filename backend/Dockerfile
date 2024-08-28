# 1단계: 빌드 단계
FROM gradle:7.6-jdk17 AS build

# 작업 디렉토리 설정
WORKDIR /app

# 소스 파일을 컨테이너로 복사
COPY . .

# Gradle을 이용해 빌드 및 실행 가능한 JAR 파일 생성
RUN gradle clean bootJar

# 2단계: 실행 단계
FROM openjdk:17-jdk-slim

# 타임존 설정을 위해 필요한 패키지 설치
RUN apt-get update && apt-get install -y tzdata

# 타임존 설정
ENV TZ=Asia/Seoul
RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일을 실행 단계로 복사
COPY --from=build /app/build/libs/newsdigest-0.0.1-SNAPSHOT.jar app.jar

# 필요한 포트를 외부에 노출
EXPOSE 8080

# 애플리케이션 실행
CMD ["java", "-jar", "/app/app.jar"]
