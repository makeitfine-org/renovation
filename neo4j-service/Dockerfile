###
#  Created under not commercial project "Renovation"
#
#  Copyright 2021-2024
###

FROM eclipse-temurin:21-jdk
MAINTAINER Kores Mosto <koresmosto@gmail.com>

VOLUME /tmp

COPY target/neo4j-service-0.0.1-SNAPSHOT.jar /app.jar

CMD ["java", "-jar", "/app.jar"]
