FROM navikt/java:17
ENV APP_NAME=familie-historikk

COPY ./target/familie-historikk.jar "app.jar"
