#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2023
#

export BACKEND_SERVER_URL=http://mib #todo: fix
export FRONTEND_INFO_SERVER_URL=http://mii #todo: fix

PROJECT_PATH="`dirname \"$0\"`"/../..

gradle -p "${PROJECT_PATH}" :api-test:build --no-build-cache
