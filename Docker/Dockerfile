FROM eclipse-temurin:21.0.2_13-jdk-alpine
MAINTAINER GEYSON GOMES <geysongomes6@gmail.com>

RUN apk add -U tzdata curl wget
RUN cp /usr/share/zoneinfo/Brazil/East /etc/localtime && apk del tzdata

EXPOSE 8080
COPY ./financeiro-service.jar app.jar
COPY ./cacerts /etc/ssl/certs/java/cacerts
COPY ./entrypoint.sh /entrypoint.sh

RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
