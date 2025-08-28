#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2025
#
CURRENT_PATH="$(realpath "$(dirname "$0")")"
CHART_PATH="${CURRENT_PATH}/../../charts"

for chart in vault-chart external-secrets-chart postgresql-chart redis-chart mongodb-chart backend info; do
  cd "$CHART_PATH/$chart" || exit 1
  echo "📦 Updating dependencies for $chart ..."
  rm Chart.lock
  helm dependency build
  helm dependency update   # (alias, both do similar things)
done

cd "$CHART_PATH/../"

rm Chart.lock
helm dependency build
helm dependency update   # (alias, both do similar things)
