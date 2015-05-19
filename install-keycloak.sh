#!/bin/bash
#
# Script to install Keycloak on a CI server.
#

set -u # Fail on unset variables
set -e # Fail if any command fails

DIST="keycloak-1.2.1.Smartling-SNAPSHOT"
ARCHIVE="${DIST}.tar.gz"

if [ ! -e ${DIST} ]; then
  wget https://s3.amazonaws.com/keycloak-server/${ARCHIVE}
  tar xzf ${ARCHIVE}
fi

cp spring-demo-realm.json /tmp

cd ${DIST}
(./bin/standalone.sh -Dkeycloak.migration.action=import \
	-Dkeycloak.migration.provider=singleFile \
	-Dkeycloak.migration.file=/tmp/spring-demo-realm.json \
	-Dkeycloak.migration.strategy=OVERWRITE_EXISTING 2>&1 > /tmp/keyloak.log &) &

