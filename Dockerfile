FROM debian:bullseye
RUN echo 'deb http://deb.debian.org/debian bullseye-backports main' > /etc/apt/sources.list.d/backports.list

ADD ./setupCuraSlicer.sh /opt/setupCuraSlicer.sh

RUN bash /opt/setupCuraSlicer.sh

ADD PrinterConfigs/RobotiklubiConf.def.json /opt/Cura/resources/definitions/RobotiklubiConf.def.json

RUN apt-get install openjdk-17-jre -y

ADD target/robotiklubi-0.0.1-SNAPSHOT.jar /app/app.jar
CMD java -Dspring.config.location=classpath:/application.properties,file:/app/application.properties -jar /app/app.jar
