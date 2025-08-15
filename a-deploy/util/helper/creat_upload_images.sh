#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2025
#
set -x // verbose commands

CURRENT_PATH="$(realpath "$(dirname "$0")")"
PROJECT_PATH="${CURRENT_PATH}/../../.."

### build and upload
# backend
cd $PROJECT_PATH
echo $(pwd)
gradle :backend:bootJar

cd $PROJECT_PATH/backend
echo $(pwd)

docker build -f no-security.Dockerfile -t koresmosto/renovation-backend:no-security .
minikube image load koresmosto/renovation-backend:no-security
minikube ssh -- "docker tag koresmosto/renovation-backend:no-security koresmosto/renovation-backend:latest"


## info
cd $PROJECT_PATH
echo $(pwd)
gradle :info:bootJar

cd $PROJECT_PATH/info
echo $(pwd)

docker build -f no-security.Dockerfile -t koresmosto/renovation-info:no-security .
minikube image load koresmosto/renovation-info:no-security
minikube ssh -- "docker tag koresmosto/renovation-info:no-security koresmosto/renovation-info:latest"
