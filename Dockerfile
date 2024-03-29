# Download opentelemetry-javaagent
# https://opentelemetry.io/docs/instrumentation/java/automatic/agent-config/
FROM scratch as javaagent
ARG JAVA_OTEL_VERSION=v1.32.0
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/$JAVA_OTEL_VERSION/opentelemetry-javaagent.jar /instrumentations/java/javaagent.jar

# Final image
FROM gcr.io/distroless/java21-debian12:nonroot
COPY --from=javaagent --chown=nonroot:nonroot /instrumentations/java/javaagent.jar /app/javaagent.jar
COPY --chown=nonroot:nonroot ./target/familie-historikk.jar /app/app.jar
WORKDIR /app

ENV APP_NAME=familie-historikk
ENV TZ="Europe/Oslo"
# TLS Config works around an issue in OpenJDK... See: https://github.com/kubernetes-client/java/issues/854
ENTRYPOINT [ "java", "-javaagent:/app/javaagent.jar", "-Djdk.tls.client.protocols=TLSv1.2", "-jar", "/app/app.jar", "-XX:MinRAMPercentage=25.0 -XX:MaxRAMPercentage=75.0 -XX:+HeapDumpOnOutOfMemoryError" ]
