FROM eclipse-temurin:21 as jre-build
COPY target/lib /usr/src/lib
COPY target/customerfacade-1.0.0-runner.jar /usr/src/
WORKDIR /usr/src/
CMD java -Xmx64m -jar customerfacade-1.0.0-runner.jar
EXPOSE 8080