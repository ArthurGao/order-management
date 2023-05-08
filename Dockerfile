FROM eclipse-temurin:17

LABEL mentainer="arthurgao@outlook.com"

WORKDIR /app

COPY target/orderEntity-management-1.0.jar /app/orderEntity-management.jar

# Set environment variables
ENV APP_MYSQL_HOST_URL=mysql
ENV APP_MYSQL_USER_NAME=test
ENV APP_MYSQL_PASSWORD=test_password

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "orderEntity-management.jar"]