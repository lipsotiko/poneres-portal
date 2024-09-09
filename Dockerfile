FROM openjdk:21
COPY target/poneres-portal-0.0.1-SNAPSHOT.jar poneres-portal-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/poneres-portal-0.0.1-SNAPSHOT.jar","--server.port=80"]
