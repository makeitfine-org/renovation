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

kubectl label namespace default istio-injection-
kubectl get namespace apps --show-labels

istioctl uninstall -y --purge

wait_for_pods_to_disappear istio-system istio-egressgateway Running
wait_for_pods_to_disappear istio-system istio-ingressgateway Running
wait_for_pods_to_disappear istio-system istiod               Running

kubectl -n apps delete pod -l app=info
kubectl -n apps delete pod -l app=backend

wait_for_pods_running_and_ready apps info
wait_for_pods_running_and_ready apps backend

timeToInit=15
echo "sleep for apps init: $timeToInit sec"
sleep $timeToInit # some time for init

sh "$CURRENT_PATH"/../test/test.sh
sh "$CURRENT_PATH"/../test/stress_test.sh

kubectl delete -f "$CURRENT_PATH/../../resources/addons"
