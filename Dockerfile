# 1. OpenJDK 17 사용
FROM eclipse-temurin:17-jdk AS build

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. JAR 파일 복사
COPY build/libs/*.jar app.jar

# 4. 컨테이너에서 실행할 명령어 지정
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "-Dspring.profiles.active=${SPRING_PROFILE}", "app.jar"]