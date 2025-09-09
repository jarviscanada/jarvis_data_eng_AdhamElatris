FROM amazoncorretto:11

WORKDIR /app

COPY target/stockquote-1.0-SNAPSHOT-jar-with-dependencies.jar /app/stockquote.jar

COPY src/main/resources/ /app/src/main/resources/

ENTRYPOINT ["java", "-jar", "/app/stockquote.jar"]