FROM openjdk:11
EXPOSE 8080
COPY target/bookstore.jar bookstore.jar
ENTRYPOINT ["java", "-jar", "/bookstore.jar"]