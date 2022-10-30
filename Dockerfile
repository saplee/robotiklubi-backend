FROM robotiklubi:latest
ADD target/robotiklubi-0.0.1-SNAPSHOT.jar /app/app.jar
CMD java -Dspring.config.location=classpath:/application.properties,file:/app/application.properties -jar /app/app.jar
