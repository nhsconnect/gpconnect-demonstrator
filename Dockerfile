# *************************************
# * Gpconnect demonstrator Dockerfile *
# *************************************

# *****************************
# * Python2 Build with Alpine *
# *****************************
FROM alpine:latest AS python2
WORKDIR /tools

# Configure and build Python 2.7.x (NodeJS Build Dependency)
#
# This can be used when we move to a more recent NodeJS version that uses Python 3
ENV LANG=C.UTF-8
ENV PYTHONIOENCODING=UTF-8

RUN apk add --no-cache ca-certificates

ENV GPG_KEY=C01E1CAD5EA2C4F0B8E3571504C367C218ADD4FF
ENV PYTHON_VERSION=2.7.18
RUN apk add --no-cache --virtual .fetch-deps gnupg tar xz \
 && wget -O python.tar.xz "https://www.python.org/ftp/python/${PYTHON_VERSION%%[a-z]*}/Python-$PYTHON_VERSION.tar.xz" \
 && wget -O python.tar.xz.asc "https://www.python.org/ftp/python/${PYTHON_VERSION%%[a-z]*}/Python-$PYTHON_VERSION.tar.xz.asc" \
 && export GNUPGHOME="$(mktemp -d)" \
 && { command -v gpgconf > /dev/null \
 && gpgconf --kill all || :; \
    } \
 && rm -rf "$GNUPGHOME" python.tar.xz.asc \
 && mkdir -p /usr/src/python \
 && tar -xJC /usr/src/python --strip-components=1 -f python.tar.xz \
 && rm python.tar.xz \
 && apk add --no-cache --virtual .build-deps  bzip2-dev coreutils dpkg-dev dpkg expat-dev findutils gcc gdbm-dev libc-dev libffi-dev libnsl-dev libtirpc-dev linux-headers make ncurses-dev openssl-dev pax-utils readline-dev sqlite-dev tcl-dev tk tk-dev zlib-dev \
 && apk del .fetch-deps \
 && cd /usr/src/python \
 && sed -i 's/typedef enum {false, true} bool;/#include <stdbool.h>/' Include/asdl.h \
 && gnuArch="$(dpkg-architecture --query DEB_BUILD_GNU_TYPE)" \
 && ./configure --build="$gnuArch" --enable-optimizations --enable-option-checking=fatal --enable-shared --enable-unicode=ucs4 --with-system-expat --with-system-ffi \
 && make -j "$(nproc)" EXTRA_CFLAGS="-DTHREAD_STACK_SIZE=0x100000" PROFILE_TASK='-m test.regrtest --pgo test_array test_base64 test_binascii test_binhex test_binop test_bytes test_c_locale_coercion test_class test_cmath test_codecs test_compile test_complex test_csv test_decimal test_dict test_float test_fstring test_hashlib test_io test_iter test_json test_long test_math test_memoryview test_pickle test_re test_set test_slice test_struct test_threading test_time test_traceback test_unicode ' \
 && make install \
 && find /usr/local -type f -executable -not \( -name '*tkinter*' \) -exec scanelf --needed --nobanner --format '%n#p' '{}' ';\n' | tr ',' '\n' | sort -u | awk 'system("[ -e /usr/local/lib/" $1 " ]") == 0 { next } { print "so:" $1 }' | xargs -rt apk add --no-cache --virtual .python-rundeps \
 && apk del .build-deps \
 && find /usr/local -depth \( \( -type d -a \( -name test -o -name tests -o -name idle_test \) \) -o \( -type f -a \( -name '*.pyc' -o -name '*.pyo' \) \) \) -exec rm -rf '{}' + \
 && rm -rf /usr/src/python \
 && python2 --version


# ***************************
# * Node8 Build with Alpine *
# ***************************
FROM python2 AS node8
WORKDIR /tools

# Configure and build Node JS
ENV NODE_VERSION=8.11.1
RUN addgroup -g 1000 node \
 && adduser -u 1000 -G node -s /bin/sh -D node \
 && apk add --no-cache libstdc++ tar xz \
 && apk add --no-cache --virtual .build-deps binutils-gold curl g++ gcc gnupg libgcc linux-headers make \
 && curl -SLO "https://nodejs.org/dist/v$NODE_VERSION/node-v$NODE_VERSION.tar.xz" \
 && tar -xf "node-v$NODE_VERSION.tar.xz" \
 && cd "node-v$NODE_VERSION" \
 && sed -i '/#include "nghttp2_helper.h"/a #include <arpa/inet.h>' deps/nghttp2/lib/nghttp2_helper.c \
 && export CFLAGS="-std=gnu11 -O2 -fcommon -Wno-error" \
 && export CXXFLAGS="-std=gnu++14 -O2 -fcommon -fpermissive -Wno-error -Wno-deprecated-copy -Wcast-function-type" \
 && export LDFLAGS="" \
 && ./configure --without-inspector \
 && make -j1 \
 && make install \
 && apk del .build-deps \
 && cd .. \
 && rm -Rf "node-v$NODE_VERSION" \
 && rm "node-v$NODE_VERSION.tar.xz"


# *************
# * GUI build *
# *************
FROM node8 AS ui-build
WORKDIR /app

RUN npm install -g npm@4.5.0 grunt-cli@1.3.2 bower
COPY ./ .
RUN apk add --no-cache optipng
RUN apk add --no-cache --virtual .js-build-deps git autoconf automake libtool g++ gcc gnupg libgcc linux-headers make

WORKDIR /app/webapp
RUN git config --global url."https://github.com/".insteadOf git://github.com/ \
 && bower install --allow-root \
 && bower update --allow-root \
 && npm update \
 && rm /app/webapp/node_modules/optipng-bin/vendor/optipng \
 && ln -sf /usr/bin/optipng /app/webapp/node_modules/optipng-bin/vendor/optipng \
 && grunt build

RUN apk del .js-build-deps

WORKDIR /app/webapp/app

CMD ["grunt", "serve"]

# ******************************
# * Springboot Maven WAR build *
# ******************************

FROM maven:3.9.16-amazoncorretto-11-alpine AS api-build
WORKDIR /app
COPY --from=ui-build /app /app
RUN javac -version
RUN mvn -e verify clean package

FROM alpine:latest
WORKDIR /app

RUN apk add openjdk11-jre

#
# Copy war as app.war and config folders
#
COPY ./config ./config
COPY --from=api-build /app/gpconnect-demonstrator-api/target/gpconnect-demonstrator-api.war ./app.war

EXPOSE 19191
EXPOSE 19192

ENV DATABASE_ADDRESS=10.100.100.61
ENV DATABASE_PORT=3306
ENV DATABASE_USERNAME=gpconnectdbuser
ENV DATABASE_PASSWORD=gpc0nn3ct
ENV DATABASE_SCHEMA=gpconnect1_5
ENV SERVER_BASE_URL=https://data.developer.nhs.uk/B82617/STU3/1/gpconnect/fhir
ENV CONTEXT_PATH=/B82617/STU3/1/gpconnect/
ENV PROPERTIES_PATHS=file:/app/config/gpconnect-demonstrator-api.properties

ENTRYPOINT java -jar /app/app.war \
 --spring.config.location=$PROPERTIES_PATHS \
 --server.port=19192 \
 --server.port.http=19191 \
 --config.path=/app/config/ \
 --server.ssl.key-store=/app/config/server.jks \
 --server.ssl.key-store-password=password \
 --server.ssl.trust-store=/app/config/server.jks \
 --server.ssl.trust-store-password=password \
 --server.ssl.client-auth=want \
 --datasource.host=$DATABASE_ADDRESS \
 --datasource.port=$DATABASE_PORT \
 --datasource.username=$DATABASE_USERNAME \
 --datasource.password=$DATABASE_PASSWORD \
 --datasource.schema=$DATABASE_SCHEMA \
 --serverBaseUrl=$SERVER_BASE_URL \
 --server.contextPath=$CONTEXT_PATH
