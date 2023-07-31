FROM adoptopenjdk/openjdk11

ARG JAR_FILE=/build/libs/chagok-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} /chagok.jar

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod", "/chagok.jar"]
