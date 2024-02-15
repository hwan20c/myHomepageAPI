FROM openjdk:17-jre-slim

WORKDIR /app
COPY build/libs/your-app.jar .

CMD ["java", "-jar", "your-app.jar"]