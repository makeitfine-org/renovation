#!/usr/bin/env bash
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2025
#

#set -x // verbose commands
#
CURRENT_PATH="$(realpath "$(dirname "$0")")"
CHART_PATH="$CURRENT_PATH/../../charts"
CLUSTER_NAME="newc";
CLUSTER_IP="192.168.49.2"

helm -n security uninstall secrets
sleep 10

helm -n security uninstall vaultrs
sleep 30
kubectl -n security delete pvc data-vaultrs-0
kubectl patch pv vault-master-0-pv -p "{\"spec\":{\"claimRef\": null}}"
kubectl get pv
sleep 10

helm -n security uninstall extrs
sleep 10

helm list -A
