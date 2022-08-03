# 베이스 이미지 +이미지 별칭
FROM adoptopenjdk/openjdk11 AS builder

# gradlew 복사
COPY gradlew .

# gradle 복사
COPY gradle gradle

#build.gradle 복사
COPY build.gradle .

#setting.gradle 복사
COPY settings.gradle .

# 웹 어플리케이션 소스 복사
COPY src src

#
RUN chmod +x ./gradlew
RUN sed -i 's/\r$//' ./gradlew
RUN ["./gradlew","bootJar"]

# 베이스 이미지
FROM adoptopenjdk:11-jdk-hotspot

COPY --from=builder build/libs/*.jar gogetter.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/gogetter.jar"]