FROM maven:3-jdk-8-alpine

WORKDIR /usr/src/app

COPY . /usr/src/app
RUN mvn package

ENV PORT 8080
EXPOSE $PORT
HEALTHCHECK CMD java -jar target/lib/healthcheck.jar http://localhost:$PORT/actuator/health

CMD [ "sh", "-c", "mvn -Dserver.port=${PORT} spring-boot:run" ]