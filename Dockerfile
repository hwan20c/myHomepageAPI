# Run Stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/app.jar .
EXPOSE 8090
CMD ["java", "-jar", "app.jar"]
