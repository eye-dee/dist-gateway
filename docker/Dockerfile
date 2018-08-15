FROM openjdk:10-jre
MAINTAINER Igor Drozdov <drozdovigor1996@gmail.com>

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/server/gateway.jar"]

COPY ./libs/dist-gateway-1.0-SNAPSHOT.jar /usr/share/server/gateway.jar