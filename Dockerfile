FROM gradle:7.6.0-jdk17-alpine as builder
WORKDIR /bucket_list/build

# 그래들 파일이 변경되었을 때만 새롭게 의존패키지 다운로드 받게함.
COPY build.gradle settings.gradle /build/
RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true

# 빌더 이미지에서 애플리케이션 빌드
COPY . /bucket_list/build
RUN gradle build -x test --parallel

# APP
FROM openjdk:17-jdk-slim
WORKDIR /bucket_list/app

# 빌더 이미지에서 jar 파일만 복사
COPY --from=builder /bucket_list/build/build/libs/*-SNAPSHOT.jar /bucket_list/app.jar

EXPOSE 8080

# root 대신 nobody 권한으로 실행
USER nobody
ENTRYPOINT [                                                \
    "java",                                                 \
    "-jar",                                                 \
    "-Djava.security.egd=file:/dev/./urandom",              \
    "-Dsun.net.inetaddr.ttl=0",                             \
    "app.jar"              \
]
