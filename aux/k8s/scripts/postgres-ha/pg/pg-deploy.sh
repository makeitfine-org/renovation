#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2023
#
K8S_PATH="`dirname \"$0\"`"/../../..

kubectl apply -f "${K8S_PATH}/yaml/postgres-ha/pg/postgres-ha-storage.yaml"
kubectl apply -f "${K8S_PATH}/yaml/postgres-ha/pg/postgres-ha-headless-svc.yaml"
kubectl apply -f "${K8S_PATH}/yaml/postgres-ha/pg/postgres-ha-statefulset.yaml"