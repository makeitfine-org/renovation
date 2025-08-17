#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2025
#

#activate in `a-deploy` dir: `source util/helper/aliases.sh`

CHART_NAME="renovation"
CHART_NAMESPACE="default"

# helm
alias hi="helm install $CHART_NAME . --namespace $CHART_NAMESPACE"
alias hid="helm install $CHART_NAME . --namespace $CHART_NAMESPACE --dry-run > dry.txt"

alias hug="helm upgrade $CHART_NAME --namespace $CHART_NAMESPACE"

alias hdu="helm dependency update --namespace $CHART_NAMESPACE"

alias rmc="rm -rf charts/*.tgz Chart.lock"

alias hun="helm uninstall $CHART_NAME --namespace $CHART_NAMESPACE"

alias hla="helm list -A --namespace $CHART_NAMESPACE"

alias hs="helm status $CHART_NAME --namespace $CHART_NAMESPACE"

# kubectl
alias kdb="kubectl get -n db all"
alias kapps="kubectl get -n apps all"
alias kpp="kubectl patch pv postgres-primary-0-pv -p '{\"spec\":{\"claimRef\": null}}'&&kubectl patch pv postgres-replica-0-pv -p '{\"spec\":{\"claimRef\": null}}'"
