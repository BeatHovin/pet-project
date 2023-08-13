FROM openjdk:20
EXPOSE 8081
ADD target/pet-project.jar pet-project.jar
ENTRYPOINT ["java", "-jar", "/pet-project.jar"]