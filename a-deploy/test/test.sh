#!/bin/sh
set -x // verbose commands

#MINIKUBE_PATH="`dirname \"$0\"`"
#K8S_PATH="${MINIKUBE_PATH}/../../k8s"
#echo "minikube path: $MINIKUBE_PATH ($(pwd))"

CLUSTER_NAME="newc";
CLUSTER_IP="192.168.49.2";

POSTGRES_PORT=30432;

REDIS_PORT=30379;
MONGO_PORT=30017;

#postgres:
#user: postgres
#pass: postgres1
#url: jdbc:postgresql://192.168.49.2:30432/renovation
#schema: backend
#
#redis:
#password: redispass
#url: jdbc:redis://192.168.49.2:30379/
#
#mongo:
#user: infouser
#pass: infopassword
#url: mongodb://192.168.49.2:30017/infodb

# test postgres

# test redis

# test mongo

# test apps:
newman run renovation-minikube.postman_collection.json -e renovation-minikube-env.postman_environment.json
