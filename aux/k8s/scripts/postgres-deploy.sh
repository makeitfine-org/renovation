#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2022
#
K8S_PATH="`dirname \"$0\"`"/..

kubectl apply -f "${K8S_PATH}/postgres/postgres-secret.yaml"
kubectl apply -f "${K8S_PATH}/postgres/postgres-configmap.yaml"
kubectl apply -f "${K8S_PATH}/postgres/postgres-storage.yaml"
kubectl apply -f "${K8S_PATH}/postgres/postgres-deployment.yaml"
kubectl apply -f "${K8S_PATH}/postgres/postgres-service.yaml"