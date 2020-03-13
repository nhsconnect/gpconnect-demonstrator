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
ENV DATABASE_USERNAME=gpconnectdbuser
ENV DATABASE_PASSWORD=gpc0nn3ct
ENV DATABASE_SCHEMA=gpconnect0_7
ENV CONTEXT_PATH=/gpconnect-demonstrator/v0_7/
ENTRYPOINT java -jar /app/app.war \
--spring.config.location=file:/app/config/gpconnect-demonstrator-api.properties --server.port=19192 \
--server.port.http=19191 --config.path=/app/config/ --server.ssl.key-store=/app/config/server.jks \
--server.ssl.key-store-password=password --server.ssl.trust-store=/app/config/server.jks \
--server.ssl.trust-store-password=password --server.ssl.client-auth=want --legacy.datasource.host=$DATABASE_ADDRESS \
--legacy.datasource.port=$DATABASE_PORT --legacy.datasource.password=$DATABASE_PASSWORD \
--legacy.datasource.username=$DATABASE_USERNAME --legacy.datasource.schema=$DATABASE_SCHEMA --server.contextPath=$CONTEXT_PATH
