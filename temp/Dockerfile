###
#  Created under not commercial project "Renovation"
#
#  Copyright 2021-2023
###

FROM openjdk:21-jdk
MAINTAINER Kores Mosto <koresmosto@gmail.com>

VOLUME /tmp

COPY build/libs/temp-0.0.1-SNAPSHOT.jar /app.jar

CMD ["java", "-jar", "-Dspring.profiles.active=vault", "/app.jar"]
