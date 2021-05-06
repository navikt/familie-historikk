FROM navikt/java:11-appdynamics

ENV APPD_ENABLED=TRUE
ENV APP_NAME=familie-historikk

COPY ./app/target/familie-historikk.jar "app.jar"
