#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2022
#
export SERVER_URL=http://192.168.49.2:30080
PROJECT_PATH="`dirname \"$0\"`"/../..

gradle -p "${PROJECT_PATH}" :backend-api-test:build --no-build-cache
