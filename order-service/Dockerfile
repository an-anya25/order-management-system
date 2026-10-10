FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/order-service-*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]