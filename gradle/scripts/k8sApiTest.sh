#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2022
#

export BACKEND_SERVER_URL=http://192.168.49.2:30080
export FRONTEND_INFO_SERVER_URL=http://192.168.49.2:30081

PROJECT_PATH="`dirname \"$0\"`"/../..

gradle -p "${PROJECT_PATH}" :api-test:build --no-build-cache
