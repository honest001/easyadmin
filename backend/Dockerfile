FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/easyadmin-1.0.0.jar /app/easyadmin.jar

EXPOSE 8081

CMD ["java", "-jar", "easyadmin.jar"]