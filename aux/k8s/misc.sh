#!/usr/bin/env bash
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2022
#

#It's good to make `mvn clean install` or `mvn clean install -DskipTests=true` on project before
echo "=================================================================="
echo "=================================================================="
echo " Remove kubernetes services, deployments and recreate them again  "
echo "=================================================================="
echo
echo

# switch to minikube cluster docker env:
eval $(minikube docker-env)

# add minikube addon to upload docker images from local env to cluster
mi addons enable registry

# upload images:
mi image load koresmosto/renovation-backend
mi image load koresmosto/renovation-info
mi image load postgres:14.2

# TODO: change for using scripts
# delete postgres:
kubectl delete service postgres
kubectl delete deployments.apps postgres
kubectl delete pvc postgres-pv-claim
kubectl delete pv postgres-pv-volume
kubectl delete configmaps renovation-configmap
kubectl delete secrets renovation-secret

kubectl apply -f aux/k8s/postgres/renovation-secret.yaml
kubectl apply -f aux/k8s/renovation-configmap.yaml
kubectl apply -f aux/k8s/postgres/postgres-storage.yaml
kubectl apply -f aux/k8s/postgres/postgres-deployment.yaml
kubectl apply -f aux/k8s/postgres/postgres-service.yaml

# see minikube service to connect
minikube service --all
