#!/usr/bin/env bash
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2023
#
set -x // verbose commands

# shellcheck disable=SC2006
MINIKUBE_PATH="`dirname \"$0\"`"
K8S_PATH="${MINIKUBE_PATH}/../k8s"

minikube start -n 2 -p mn --memory 6144 --cpus 4 --subnet 192.168.49.0

minikube -p mn addons enable volumesnapshots
minikube -p mn addons enable registry
minikube -p mn addons enable metrics-server
minikube -p mn addons enable ingress-dns
minikube -p mn addons enable ingress
minikube -p mn addons enable dashboard

minikube -p mn image load koresmosto/renovation-backend
minikube -p mn image load koresmosto/renovation-frontend-info
minikube -p mn image load koresmosto/renovation-info

#create folders inside minikube ssh
minikube -p mn cp "${MINIKUBE_PATH}/create-mnt-content.sh" mn:/home/docker/create-mnt-content.sh
minikube -p mn ssh <<'ENDSSH'
sudo sh /home/docker/create-mnt-content.sh
exit
ENDSSH

minikube -p mn cp "${MINIKUBE_PATH}/create-mnt-content.sh" mn-m02:/home/docker/create-mnt-content.sh
minikube -p mn -n mn-m02 ssh <<'ENDSSH'
sudo sh /home/docker/create-mnt-content.sh
exit
ENDSSH

kubectl apply -f "${K8S_PATH}/yaml/renovation-namespace.yaml"

kubectl config set-context --current --namespace=renovation
sh "${K8S_PATH}/scripts/deploy-all.sh"
kubectl config set-context --current --namespace=default
sleep 45

echo "PROMETHEUS and GRAFANA deploying:"
kubectl apply -f "${K8S_PATH}/yaml/metrics/monitoring-namespace.yaml"

helm install prometheus bitnami/kube-prometheus -n monitoring --set prometheus.service.type=NodePort --set prometheus.service.nodePorts.http=31909
sleep 45

helm install grafana bitnami/grafana --set grafana.nodeSelector."kubernetes\.io/hostname"=mn -n monitoring --set service.type=NodePort --set service.nodePorts.grafana=31300
sleep 45

echo "=== PASSWORD (grafana) ==="
echo "$(kubectl get secret grafana-admin --namespace monitoring -o jsonpath="{.data.GF_SECURITY_ADMIN_PASSWORD}" | base64 --decode)"

echo "=== COMMANDS for PROMETHEUS and GRAFANA connections: ==="
echo "kubectl port-forward -n monitoring svc/prometheus-kube-prometheus-prometheus 9090:9090"
echo "export POD_NAME=$(kubectl get pods -n monitoring -l "app.kubernetes.io/name=grafana,app.kubernetes.io/instance=grafana" -o jsonpath="{.items[0].metadata.name}") && kubectl -n monitoring port-forward $POD_NAME 3000"
echo "=== ==="

kubectl apply -f "${K8S_PATH}/yaml/metrics/metrics-monitor.yaml" -n monitoring
echo "!!! DEPLOYED !!!"
