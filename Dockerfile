# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS build
COPY . /app
WORKDIR /app

# Step 1 Build
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
