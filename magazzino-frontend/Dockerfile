# Stage 1: Runtime con Java 25
FROM eclipse-temurin:25-jdk

# Impostazioni WildFly
ENV WILDFLY_VERSION 34.0.0.Final
ENV JBOSS_HOME /opt/jboss/wildfly

USER root
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Scarichiamo e installiamo WildFly
RUN mkdir -p $JBOSS_HOME \
    && curl -L https://github.com/wildfly/wildfly/releases/download/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz | tar zx --strip-components=1 -C $JBOSS_HOME

# Copiamo l'EAR generato da Maven
COPY magazzino-ear/target/*.ear $JBOSS_HOME/standalone/deployments/

# Esponiamo le porte (8080 app, 9990 management)
EXPOSE 8080 9990

# Avvio WildFly in modalità standalone
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]