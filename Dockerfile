FROM --platform=linux/amd64 eclipse-temurin:17-jre-alpine
ADD target/robotiklubi-0.0.1-SNAPSHOT.jar /app/app.jar
CMD java -Dspring.config.location=classpath:/application.properties,file:/app/application.properties -jar /app/app.jar
