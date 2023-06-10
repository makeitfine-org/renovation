#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2023
#
K8S_SCRIPTS_PATH="`dirname \"$0\"`"

#sh "${K8S_SCRIPTS_PATH}/configs/configs-deploy.sh"
sh "${K8S_SCRIPTS_PATH}/pg/pg-deploy.sh"
sh "${K8S_SCRIPTS_PATH}/pgpool/pgpool-deploy.sh"

