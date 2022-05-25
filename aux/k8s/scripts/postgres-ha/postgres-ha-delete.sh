#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2022
#
K8S_SCRIPTS_PATH="`dirname \"$0\"`"

sh "${K8S_SCRIPTS_PATH}/pgpool/pgpool-delete.sh"
sh "${K8S_SCRIPTS_PATH}/pg/pg-delete.sh"
#sh "${K8S_SCRIPTS_PATH}/configs/configs-delete.sh"
