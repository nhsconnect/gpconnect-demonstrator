FROM openjdk:8-jdk

ENV NODE_VERSION 6.10.2

RUN mkdir -p /usr/src/app && \
    apt-get update -y && \
    apt-get install maven -y && \
    curl -SLO "https://nodejs.org/dist/v$NODE_VERSION/node-v$NODE_VERSION-linux-x64.tar.xz" && \
    tar -xJf "node-v$NODE_VERSION-linux-x64.tar.xz" -C /usr/local --strip-components=1 && \
    rm "node-v$NODE_VERSION-linux-x64.tar.xz" && \
    npm install -g grunt-cli bower

WORKDIR /usr/src/app
ADD . /usr/src/app

RUN cd /usr/src/app/webapp && \
    bower install --allow-root && \
    bower update --allow-root && \
    npm update && \
    cd /usr/src/app && \
    mvn clean package