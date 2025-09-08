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

wait_for_pods_to_disappear() {
  local NAMESPACE="$1"
  local PATTERN="$2"

  while kubectl get pods -n "$NAMESPACE" 2>/dev/null | grep -q "$PATTERN"; do
    echo "Waiting for pods matching '$PATTERN' in namespace '$NAMESPACE' to disappear..."
    sleep 2
  done

  echo "✅ No pods matching '$PATTERN' remain in namespace '$NAMESPACE'!"
}


# kubectl apply -f "$CHART_PATH/mongodb-chart/resources/mongodb-pv.yaml"
# kubectl apply -f "$CHART_PATH/redis-chart/resources/redis-pv.yaml"

# postgres
#helm uninstall -n db postgresrs
#sleep 30
#
#kubectl -n db delete pvc postgres-primary-0-pvc
#kubectl -n db delete pvc postgres-replica-0-pvc
#
#kubectl patch pv postgres-primary-0-pv -p "{\"spec\":{\"claimRef\": null}}"
#kubectl patch pv postgres-replica-0-pv -p "{\"spec\":{\"claimRef\": null}}"
#kubectl get pv
#sleep 10
#
## secrets
#helm -n security uninstall secrets
#sleep 10
#
## vault
#helm -n security uninstall vaultrs
#sleep 30
#
#kubectl -n security delete pvc data-vaultrs-0
#kubectl patch pv vault-master-0-pv -p "{\"spec\":{\"claimRef\": null}}"
#kubectl get pv
#sleep 10

# extrs
#helm -n security uninstall extrs

wait_for_pods_to_disappear security extrs-external-secrets

#sleep 10

echo "### helm list -A"
helm list -A
