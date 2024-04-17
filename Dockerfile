FROM openjdk:17-jdk-oracle
COPY ./target/task-management-system-1.0.jar task-management-system-1.0.jar
ENTRYPOINT ["java", "-jar", "task-management-system-1.0.jar"]