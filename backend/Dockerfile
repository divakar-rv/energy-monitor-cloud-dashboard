# Multi-stage build: first stage compiles the app, second stage runs it.
# This keeps the final image small -- it doesn't carry Maven or source
# code around at runtime, just the compiled .jar.

# ---- Stage 1: Build ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ---- Stage 2: Run ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/energy-monitor-1.0.0.jar app.jar

# Render sets the PORT environment variable automatically; our app
# already reads server.port from application.properties, and Render
# maps traffic to whatever port the app listens on.
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]