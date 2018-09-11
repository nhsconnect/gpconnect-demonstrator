FROM node:carbon AS ui-build
WORKDIR /app
RUN apt-get update && apt-get install git-core
RUN npm install -g npm@4.5.0 grunt-cli bower
COPY ./ .
RUN cd /app/webapp && pwd && ls && bower install --allow-root && bower update --allow-root && npm update && grunt build 

FROM maven:alpine AS api-build
WORKDIR /app
COPY --from=ui-build /app /app
RUN mvn verify clean package

FROM openjdk:alpine
WORKDIR /app

COPY ./config ./config
COPY --from=api-build /app/gpconnect-demonstrator-api/target/gpconnect-demonstrator-api.war ./app.war
EXPOSE 19191
EXPOSE 19192
ENV DATABASE_ADDRESS 10.100.100.61
ENV DATABASE_PORT 3306
ENV DATABASE_USERNAME gpconnectdbuser
ENV DATABASE_PASSWORD gpc0nn3ct
ENV DATABASE_SCHEMA gpconnect
ENV SERVER_BASE_URL https://data.developer.nhs.uk/gpconnect-demonstrator/v1/fhir
ENV CONTEXT_PATH /gpconnect-demonstrator/v1/
ENTRYPOINT java -jar /app/app.war \
--spring.config.location=file:/app/config/gpconnect-demonstrator-api.properties --server.port=19192 \
--server.port.http=19191 --config.path=/app/config/ --server.ssl.key-store=/app/config/server.jks \
--server.ssl.key-store-password=password --server.ssl.trust-store=/app/config/server.jks \
--server.ssl.trust-store-password=password --server.ssl.client-auth=want --datasource.host=$DATABASE_ADDRESS \
--datasource.port=$DATABASE_PORT --datasource.username=$DATABASE_USERNAME --datasource.password=gpc0nn3ct \
--datasource.schema=$DATABASE_SCHEMA --serverBaseUrl=$SERVER_BASE_URL ==server.contextPath=$CONTEXT_PATH