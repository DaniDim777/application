FROM maven:3.8.6-jdk-11
ADD . /home/workdir/
WORKDIR /home/workdir/
RUN mvn install -Dmaven.test.skip=true

FROM openjdk:11

COPY --from=0 /home/workdir/app/target/app-0.0.1-SNAPSHOT.jar /home/workdir/app.jar
WORKDIR /home/workdir/
CMD ["java", "-jar", "app.jar"]