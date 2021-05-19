#!/bin/bash

DB_CERT_HOST=localhost:30036
DB_CERT_DB=certification
DB_CERT_USER=certification
DB_CERT_PASS=certification
docker build -t cert .

echo $DB_CERT_HOST

docker run -d -p 8080:8080 \
    --env DB_CERT_HOST=$DB_CERT_HOST --env DB_CERT_DB=$DB_CERT_DB \
    --env DB_CERT_USER=$DB_CERT_USER --env DB_CERT_PASS=$DB_CERT_PASS \
    --name cert cert

docker logs -f cert