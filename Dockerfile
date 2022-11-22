FROM mloomets/robotiklubi@sha256:546d8cd3e3c8c64064ff9d425c143bc11313648728d5b6b332c667cd291015f2
ADD PrinterConfigs/* /opt/PrinterConfigs/
ADD target/robotiklubi-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT []
CMD java -Dspring.config.location=classpath:/application.properties,file:/app/application.properties -jar /app/app.jar