FROM openjdk:19-jdk-alpine
WORKDIR /app

COPY build/libs/blog.jar /app/blog.jar

EXPOSE 8080

CMD ["java", "-jar", "blog.jar"]

