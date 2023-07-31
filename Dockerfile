FROM adoptopenjdk/openjdk11

ARG JAR_FILE=*.jar

COPY ${JAR_FILE} /chagok.jar

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod", "/chagok.jar"]
