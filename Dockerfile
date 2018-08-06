FROM node:carbon AS ui-build
WORKDIR /app

RUN apt-get update && apt-get install git-core
RUN npm install -g npm@4.5.0 grunt-cli bower

COPY ./webapp .
RUN bower install --allow-root
RUN bower update --allow-root
RUN npm update
RUN grunt build 

FROM maven:alpine AS api-build
WORKDIR /app

COPY pom.xml .
COPY gpconnect-core/pom.xml ./gpconnect-core/pom.xml
COPY gpconnect-database/pom.xml ./gpconnect-database/pom.xml
COPY gpconnect-demonstrator-api/pom.xml ./gpconnect-demonstrator-api/pom.xml

COPY ./gpconnect-core ./gpconnect-core
COPY ./gpconnect-database ./gpconnect-database
COPY ./gpconnect-demonstrator-api ./gpconnect-demonstrator-api
COPY --from=ui-build ./gpconnect-demonstrator-api/src/main/webapp /app/dist 
RUN mvn verify clean package

FROM openjdk:alpine
WORKDIR /app

COPY ./config ./config
COPY --from=api-build /app/gpconnect-demonstrator-api/target/gpconnect-demonstrator-api.war ./app.war
EXPOSE 19191
EXPOSE 19192
ENV DATABASE_ADDRESS 10.100.100.61
ENV DATABASE_PORT 3306
ENTRYPOINT java -jar /app/app.war \
--spring.config.location=file:/app/config/gpconnect-demonstrator-api.properties --server.port=19192 \
--server.port.http=19191 --config.path=/app/config/ --server.ssl.key-store=/app/config/server.jks \
--server.ssl.key-store-password=password --server.ssl.trust-store=/app/config/server.jks \
--server.ssl.trust-store-password=password --server.ssl.client-auth=want --datasource.host=$DATABASE_ADDRESS \
--datasource.port=$DATABASE_PORT