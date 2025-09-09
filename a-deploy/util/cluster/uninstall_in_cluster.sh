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

wait_for_secret_to_disappear() {
  local NAMESPACE="$1"
  local PATTERN="$2"
  local ENTITY="$3"

  while kubectl get "$ENTITY" -n "$NAMESPACE" 2>/dev/null | grep -q "$PATTERN"; do
    echo "Waiting for '$ENTITY' matching '$PATTERN' in namespace '$NAMESPACE' to disappear..."
    sleep 2
  done

  echo "✅ No '$ENTITY'(s) matching '$PATTERN' remain in namespace '$NAMESPACE'!"
}

# kubectl apply -f "$CHART_PATH/mongodb-chart/resources/mongodb-pv.yaml"
# kubectl apply -f "$CHART_PATH/redis-chart/resources/redis-pv.yaml"

## info (uninstall)
helm uninstall -n apps infors
wait_for_pods_to_disappear apps info

## backend (uninstall)
helm uninstall -n apps backendrs
wait_for_pods_to_disappear apps backend

## mongodb (uninstall)
helm -n db uninstall mongodbrs
wait_for_pods_to_disappear db mongodb-master

kubectl -n db delete pvc mongodb-master-0-pvc

kubectl patch pv mongodb-master-0-pv -p "{\"spec\":{\"claimRef\": null}}"
kubectl get pv

## redis (uninstall)
helm -n db uninstall redisrs
wait_for_pods_to_disappear db redis-master
wait_for_pods_to_disappear db redis-replica

kubectl -n db delete pvc redis-master-0-pvc
kubectl -n db delete pvc redis-replica-0-pvc

kubectl patch pv redis-master-0-pv -p "{\"spec\":{\"claimRef\": null}}"
kubectl patch pv redis-replica-0-pv -p "{\"spec\":{\"claimRef\": null}}"
kubectl get pv

## postgres (uninstall)
helm -n db uninstall postgresrs
wait_for_pods_to_disappear db postgresql-primary
wait_for_pods_to_disappear db postgresql-read

kubectl -n db delete pvc postgres-primary-0-pvc
kubectl -n db delete pvc postgres-replica-0-pvc

kubectl patch pv postgres-primary-0-pv -p "{\"spec\":{\"claimRef\": null}}"
kubectl patch pv postgres-replica-0-pv -p "{\"spec\":{\"claimRef\": null}}"
kubectl get pv

## secrets (uninstall)
helm -n security uninstall secrets
wait_for_secret_to_disappear apps extsecrets-redis-secret secrets
wait_for_secret_to_disappear db extsecrets-redis-secret secrets

wait_for_secret_to_disappear apps extsecrets-mongodb-secret secrets
wait_for_secret_to_disappear db extsecrets-mongodb-secret secrets

wait_for_secret_to_disappear apps extsecrets-postgresql-secret secrets
wait_for_secret_to_disappear db extsecrets-postgresql-secret secrets

## vault (uninstall)
helm -n security uninstall vaultrs
wait_for_pods_to_disappear security vaultrs

kubectl -n security delete pvc data-vaultrs-0
kubectl patch pv vault-master-0-pv -p "{\"spec\":{\"claimRef\": null}}"
kubectl get pv

## extrs (uninstall)
helm -n security uninstall extrs
wait_for_pods_to_disappear security extrs-external-secrets

echo "### helm list -A"
helm list -A
