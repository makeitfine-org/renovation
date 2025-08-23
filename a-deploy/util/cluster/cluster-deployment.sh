#!/usr/bin/env bash
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2025
#

set -x // verbose commands

# shellcheck disable=SC2006
MINIKUBE_PATH="`dirname \"$0\"`"
CLUSTER_NAME="newc";
#K8S_PATH="${MINIKUBE_PATH}/../../k8s"
echo "minikube path: $MINIKUBE_PATH ($(pwd))"

#(if necessary delete previously network)
#`docker network ls` and with `docker network inspect ...` to wind (network with 192.168.49.*)
#docker network rm <network_name> (network with 192.168.49.*)

minikube start -p ${CLUSTER_NAME} --memory 4096 --cpus 4  --subnet 192.168.49.2

# install addons
minikube -p ${CLUSTER_NAME} addons enable volumesnapshots
minikube -p ${CLUSTER_NAME} addons enable registry
minikube -p ${CLUSTER_NAME} addons enable metrics-server
minikube -p ${CLUSTER_NAME} addons enable ingress-dns
minikube -p ${CLUSTER_NAME} addons enable ingress
minikube -p ${CLUSTER_NAME} addons enable dashboard

minikube -p ${CLUSTER_NAME} image load koresmosto/renovation-backend:no-security
minikube -p ${CLUSTER_NAME} image load koresmosto/renovation-info:no-security
#minikube -p newc image load koresmosto/renovation-frontend-info # todo: not used, it could be removed
minikube -p ${CLUSTER_NAME} ssh -- "docker tag koresmosto/renovation-backend:no-security koresmosto/renovation-backend:latest"
minikube -p ${CLUSTER_NAME} ssh -- "docker tag koresmosto/renovation-info:no-security koresmosto/renovation-info:latest"

# postgres
minikube -p ${CLUSTER_NAME} ssh -- "sudo mkdir /mnt/data/postgres"
minikube -p ${CLUSTER_NAME} ssh -- "sudo mkdir /mnt/data/postgres/master"
minikube -p ${CLUSTER_NAME} ssh -- "sudo chown -R 1001:1001 /mnt/data/postgres/master"
minikube -p ${CLUSTER_NAME} ssh -- "sudo mkdir /mnt/data/postgres/replica"
minikube -p ${CLUSTER_NAME} ssh -- "sudo chown -R 1001:1001 /mnt/data/postgres/replica"

# mongodb
minikube -p ${CLUSTER_NAME} ssh -- "sudo mkdir /mnt/data/mongodb"
minikube -p ${CLUSTER_NAME} ssh -- "sudo mkdir /mnt/data/mongodb/master"
minikube -p ${CLUSTER_NAME} ssh -- "sudo chown -R 1001:1001 /mnt/data/mongodb/master"

# redis
minikube -p ${CLUSTER_NAME} ssh -- "sudo mkdir /mnt/data/redis"
minikube -p ${CLUSTER_NAME} ssh -- "sudo mkdir /mnt/data/redis/master"
minikube -p ${CLUSTER_NAME} ssh -- "sudo chown -R 1001:1001 /mnt/data/redis/master"
minikube -p ${CLUSTER_NAME} ssh -- "sudo mkdir /mnt/data/redis/replica"
minikube -p ${CLUSTER_NAME} ssh -- "sudo chown -R 1001:1001 /mnt/data/redis/replica"

# vault
minikube -p ${CLUSTER_NAME} ssh -- "sudo mkdir /mnt/data/vault"
minikube -p ${CLUSTER_NAME} ssh -- "sudo mkdir /mnt/data/vault/master"
minikube -p ${CLUSTER_NAME} ssh -- "sudo chown 777 /mnt/data/vault"
minikube -p ${CLUSTER_NAME} ssh -- "sudo chown 777 /mnt/data/vault/master"

minikube -p ${CLUSTER_NAME} ssh -- "docker images | grep koresmosto"

# switch profile to "${CLUSTER_NAME}" as default
minikube profile ${CLUSTER_NAME}

# create storage
kubectl apply -f "../../resource/vault/vault-sc.yaml"

# create pv
kubectl apply -f "../../resource/postgres/postgres-pv.yaml"
kubectl apply -f "../../resource/mongodb/mongodb-pv.yaml"
kubectl apply -f "../../resource/redis/redis-pv.yaml"
kubectl apply -f "../../resource/vault/vault-pv.yaml"

# install all
helm install renovation .

#some check in a while in browser:
# http://192.168.49.2:30080/fe/work
# http://192.168.49.2:30080/fe/worker
#http://192.168.49.2:30090/graphiql?query=%23%20Welcome%20to%20GraphiQL%0A%23%0A%23%20GraphiQL%20is%20an%20in-browser%20tool%20for%20writing%2C%20validating%2C%20and%0A%23%20testing%20GraphQL%20queries.%0A%23%0A%23%20Type%20queries%20into%20this%20side%20of%20the%20screen%2C%20and%20you%20will%20see%20intelligent%0A%23%20typeaheads%20aware%20of%20the%20current%20GraphQL%20type%20schema%20and%20live%20syntax%20and%0A%23%20validation%20errors%20highlighted%20within%20the%20text.%0A%23%0A%23%20GraphQL%20queries%20typically%20start%20with%20a%20%22%7B%22%20character.%20Lines%20that%20start%0A%23%20with%20a%20%23%20are%20ignored.%0A%23%0A%23%20An%20example%20GraphQL%20query%20might%20look%20like%3A%0A%23%0A%23%20%20%20%20%20%7B%0A%23%20%20%20%20%20%20%20field(arg%3A%20%22value%22)%20%7B%0A%23%20%20%20%20%20%20%20%20%20subField%0A%23%20%20%20%20%20%20%20%7D%0A%23%20%20%20%20%20%7D%0A%23%0A%23%20Keyboard%20shortcuts%3A%0A%23%0A%23%20%20%20Prettify%20query%3A%20%20Shift-Ctrl-P%20(or%20press%20the%20prettify%20button)%0A%23%0A%23%20%20Merge%20fragments%3A%20%20Shift-Ctrl-M%20(or%20press%20the%20merge%20button)%0A%23%0A%23%20%20%20%20%20%20%20%20Run%20Query%3A%20%20Ctrl-Enter%20(or%20press%20the%20play%20button)%0A%23%0A%23%20%20%20%20Auto%20Complete%3A%20%20Ctrl-Space%20(or%20just%20start%20typing)%0A%23%0A%0A%20query%7B%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20details%7B%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20id%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20name%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20surname%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20age%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20%7D%0A%20%20%20%20%20%20%20%20%20%20%20%20%7D

#check redis (in log should be address only once to db)
#http http://192.168.49.2:30080/api/work
#http http://192.168.49.2:30080/api/work/55555555-a845-45d7-aea9-ab624172d1c1
