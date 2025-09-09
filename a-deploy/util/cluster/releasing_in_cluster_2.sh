#!/usr/bin/env bash
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2025
#

#set -x // verbose commands
#
CURRENT_PATH="$(realpath "$(dirname "$0")")"
CLUSTER_NAME="newc";
CLUSTER_IP="192.168.49.2"

# shellcheck disable=SC2317
echo "minikube path: $MINIKUBE_PATH ($(pwd))"

minikube profile "$CLUSTER_NAME"

source "$CURRENT_PATH/utils.sh"

if ! command -v istioctl > /dev/null; then
  echo "❌ istioctl is not installed or not in PATH."

  # install istioctl
  #  curl -sL https://istio.io/downloadIstioctl | sh -
  #  export PATH=$HOME/.istioctl/bin:$PATH

  exit 1
else
  echo "✅ istioctl is installed: $(vault --version)"
fi

istioctl install --set profile=demo -y

wait_for_pod istio-system istio-egressgateway Running
wait_for_pod istio-system istio-ingressgateway Running
wait_for_pod istio-system istiod               Running

kubectl label namespace apps istio-injection=enabled --overwrite
kubectl get namespace -L istio-injection

timeToInit=15
echo "sleep for apps init: $timeToInit sec"
sleep $timeToInit # some time for init

kubectl -n apps delete pod -l app=info
kubectl -n apps delete pod -l app=backend

wait_for_pods_running_and_ready apps info
wait_for_pods_running_and_ready apps backend

timeToInit=15
echo "sleep for apps init: $timeToInit sec"
sleep $timeToInit # some time for init

sh "$CURRENT_PATH"/../test/test.sh
sh "$CURRENT_PATH"/../test/stress_test.sh

kubectl apply -f "$CURRENT_PATH/../../resources/addons"

kubectl apply -f "$CURRENT_PATH/../../resources/addons/configs"

# run:
# istioctl dashboard kiali

# istioctl dashboard prometheus
# see all metrics: http://localhost:9090/metrics
# in prometheus dashboard enter "istio_requests_total"
# see istio metrics: kubectl -n default port-forward productpage-v1-54bb874995-qpxfd 15090:15090
# http://localhost:15090/stats/prometheus

# istioctl dashboard jaeger

# istioctl dashboard grafana
