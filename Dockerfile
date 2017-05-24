FROM anapsix/alpine-java:8
ADD app-*-spring-boot.jar app.jar
CMD ["java", "-jar", "app.jar"]