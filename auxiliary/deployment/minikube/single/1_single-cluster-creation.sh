#!/usr/bin/env bash
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2024
#
set -x // verbose commands

# shellcheck disable=SC2006
MINIKUBE_PATH="`dirname \"$0\"`"
K8S_PATH="${MINIKUBE_PATH}/../../k8s"

minikube start -p single --memory 6144 --cpus 4 --subnet 192.168.58.2

minikube -p single addons enable volumesnapshots
minikube -p single addons enable registry
minikube -p single addons enable metrics-server
minikube -p single addons enable ingress-dns
minikube -p single addons enable ingress
minikube -p single addons enable dashboard

minikube -p single image load koresmosto/renovation-backend:no-security
minikube -p single image load koresmosto/renovation-info:no-security
minikube -p single image load koresmosto/renovation-frontend-info # todo: not used, it could be removed
minikube -p single ssh -- "docker tag koresmosto/renovation-backend:no-security koresmosto/renovation-backend:latest"
minikube -p single ssh -- "docker tag koresmosto/renovation-info:no-security koresmosto/renovation-info:latest"

### create folders inside minikube ssh
minikube -p single cp "${MINIKUBE_PATH}/util/single-cluster-content-creation.sh" single:/home/docker/single-cluster-content-creation.sh
minikube -p single ssh -- "sudo apt update -y"
minikube -p single ssh -- "sudo sh /home/docker/single-cluster-content-creation.sh"
minikube -p single ssh -- "sudo apt install -y net-tools"
minikube -p single ssh -- "sudo apt update -y"

kubectl apply -f "${K8S_PATH}/yaml/renovation-namespace.yaml"

kubectl config set-context --current --namespace=renovation
sh "${K8S_PATH}/scripts/deploy-all.sh"
kubectl config set-context --current --namespace=default
sleep 60

echo "=== PROMETHEUS and GRAFANA deploying ==="
kubectl apply -f "${K8S_PATH}/yaml/metrics/monitoring-namespace.yaml"

helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
helm repo list

helm install prometheus bitnami/kube-prometheus -n monitoring --set prometheus.service.type=NodePort --set prometheus.service.nodePorts.http=31909
sleep 45

helm install grafana bitnami/grafana --set grafana.nodeSelector."kubernetes\.io/hostname"=single -n monitoring --set service.type=NodePort --set service.nodePorts.grafana=31300
sleep 45

echo "=== PASSWORD (grafana) ==="
# shellcheck disable=SC2005
echo "$(kubectl get secret grafana-admin --namespace monitoring -o jsonpath="{.data.GF_SECURITY_ADMIN_PASSWORD}" | base64 --decode)"

echo "=== COMMANDS for PROMETHEUS and GRAFANA connections ==="
echo "kubectl port-forward -n monitoring svc/prometheus-kube-prometheus-prometheus 9090:9090"
echo "export POD_NAME=$(kubectl get pods -n monitoring -l "app.kubernetes.io/name=grafana,app.kubernetes.io/instance=grafana" -o jsonpath="{.items[0].metadata.name}") && kubectl -n monitoring port-forward $POD_NAME 3000"

echo "=== METRICS ==="
kubectl apply -f "${K8S_PATH}/yaml/metrics/metrics-monitor.yaml" -n monitoring

echo "=== !!! DEPLOYED !!! ==="
