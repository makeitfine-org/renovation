#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2022
#
K8S_PATH="`dirname \"$0\"`"/../../..

kubectl apply -f "${K8S_PATH}/yaml/postgres-ha/configs/postgres-ha-configmap.yaml"
kubectl apply -f "${K8S_PATH}/yaml/postgres-ha/configs/postgres-ha-secrets.yaml"
kubectl apply -f "${K8S_PATH}/yaml/postgres-ha/configs/pgpool-secret.yaml"