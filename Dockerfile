FROM openjdk:21
MAINTAINER meraklis.io
COPY target/i-care-0.0.1-SNAPSHOT.jar i-care-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/i-care-0.0.1-SNAPSHOT.jar","--server.port=80"]
