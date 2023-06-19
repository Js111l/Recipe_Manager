FROM openjdk:17
ADD ./build/libs/recipemanager-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

