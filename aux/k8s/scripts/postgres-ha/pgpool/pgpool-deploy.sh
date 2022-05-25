#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2022
#
K8S_PATH="`dirname \"$0\"`"/../../..

kubectl apply -f "${K8S_PATH}/yaml/postgres-ha/pgpool/pgpool-svc.yaml"
kubectl apply -f "${K8S_PATH}/yaml/postgres-ha/pgpool/pgpool-svc-external.yaml"
kubectl apply -f "${K8S_PATH}/yaml/postgres-ha/pgpool/pgpool-deployment.yaml"
