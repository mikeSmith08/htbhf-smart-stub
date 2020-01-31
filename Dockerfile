FROM openjdk:14-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

EXPOSE 8120

COPY build/libs/htbhf-smart-stub-0.0.1-SNAPSHOT.jar smart-stub.jar

ENTRYPOINT ["java", "-jar","smart-stub.jar"]