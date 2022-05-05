#!/usr/bin/env bash
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2022
#

K8S_SCRIPTS_PATH="`dirname \"$0\"`"

sh "${K8S_SCRIPTS_PATH}/config/config-deploy.sh"
sh "${K8S_SCRIPTS_PATH}/postgres/postgres-deploy.sh"
sh "${K8S_SCRIPTS_PATH}/mongo/mongo-deploy.sh"
sh "${K8S_SCRIPTS_PATH}/backend/backend-deploy.sh"
sh "${K8S_SCRIPTS_PATH}/info/info-deploy.sh"
