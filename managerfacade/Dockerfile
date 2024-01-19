FROM eclipse-temurin:21 as jre-build
COPY target/lib /usr/src/lib
COPY target/managerfacade-1.0.0-runner.jar /usr/src/
WORKDIR /usr/src/
CMD java -Xmx64m -jar managerfacade-1.0.0-runner.jar
EXPOSE 8084