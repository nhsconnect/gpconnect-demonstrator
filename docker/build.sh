#!/bin/bash

set -x
set -e

# By putting the create_database.sql script in this directory the mysql Docker image will run it on startup
mkdir -p docker-entrypoint-initdb.d
cp config/sql/create_database.sql docker-entrypoint-initdb.d/create_database.sql

# The only property we need to override is the MySQL host
echo "legacy.datasource.host = mysql" > config/gpconnect-demonstrator-api.environment.properties

# Build all the containers
docker-compose -f docker/docker-compose.yaml build

# Need to start up the MySQL container so that the database gets created
docker-compose -f docker/docker-compose.yaml up -d mysql

# We don't need this anymore
rm -r docker-entrypoint-initdb.d

echo "Build successful"