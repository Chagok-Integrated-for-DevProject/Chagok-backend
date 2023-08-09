FROM adoptopenjdk/openjdk11


COPY ./build/libs/chagok-0.0.1-SNAPSHOT.jar chagok.jar

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod", "/chagok.jar"]
