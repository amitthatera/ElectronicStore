FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} electronic_store.jar
ENTRYPOINT ["java","-jar","/electronic_store.jar"]