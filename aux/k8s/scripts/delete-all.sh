#!/usr/bin/env bash
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2022
#

K8S_SCRIPTS_PATH="`dirname \"$0\"`"

sh "${K8S_SCRIPTS_PATH}/ingress/ingress-delete.sh"
sh "${K8S_SCRIPTS_PATH}/frontend-info/frontend-info-delete.sh"
sh "${K8S_SCRIPTS_PATH}/info/info-delete.sh"
sh "${K8S_SCRIPTS_PATH}/backend/backend-delete.sh"
sh "${K8S_SCRIPTS_PATH}/redis/redis-delete.sh"
sh "${K8S_SCRIPTS_PATH}/postgres/postgres-delete.sh"
sh "${K8S_SCRIPTS_PATH}/mongo/mongo-delete.sh"
sh "${K8S_SCRIPTS_PATH}/config/config-delete.sh"
