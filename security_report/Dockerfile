FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/Security_report-jar-with-dependencies.jar keylogger.jar

ENTRYPOINT ["java", "-jar", "keylogger.jar"]
