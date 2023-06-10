#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2023
#
K8S_PATH="`dirname \"$0\"`"/../..

kubectl apply -f "${K8S_PATH}/yaml/info/info-deployment.yaml"
kubectl apply -f "${K8S_PATH}/yaml/info/info-service.yaml"