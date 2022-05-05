#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2022
#
K8S_PATH="`dirname \"$0\"`"/../..

kubectl apply -f "${K8S_PATH}/info/info-deployment.yaml"
kubectl apply -f "${K8S_PATH}/info/info-service.yaml"