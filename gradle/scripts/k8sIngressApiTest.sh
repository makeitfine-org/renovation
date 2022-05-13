#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2022
#

export SERVER_URL=http://mib
export FRONTEND_INFO_SERVER_URL=http://mii

PROJECT_PATH="`dirname \"$0\"`"/../..

gradle -p "${PROJECT_PATH}" :backend-api-test:build --no-build-cache
