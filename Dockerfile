FROM adoptopenjdk/openjdk11


COPY *.jar chagok.jar

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod", "/chagok.jar"]
