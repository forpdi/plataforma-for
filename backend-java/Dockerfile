FROM maven:3.6.0-jdk-8-slim as maven

COPY . /tmp/build

WORKDIR /tmp/build


RUN mvn clean package -P dev 

FROM jboss/wildfly:9.0.2.Final

COPY --from=maven tmp/build/target/forpdi.war /opt/jboss/wildfly/standalone/deployments/

COPY standalone.xml /opt/jboss/wildfly/standalone/configuration/