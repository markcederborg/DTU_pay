FROM adoptopenjdk:11-jre-hotspot
COPY target/lib /usr/src/lib
COPY target/paymentservice-1.0.0.jar /usr/src/
WORKDIR /usr/src/
CMD java -Xmx64m -jar paymentservice-1.0.0.jar
