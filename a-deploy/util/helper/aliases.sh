#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2025
#

#activate in `a-deploy` dir: `source util/helper/aliases.sh`

#CHART_NAME="renovation"
#CHART_NAMESPACE="default"

# helm
alias hi="helm install . "
alias hid="helm install --dry-run > util/x-example/dry.txt"

alias hug="helm upgrade . "

alias hdu="helm dependency update"

#alias rmc="rm -rf charts/*.tgz Chart.lock"

alias hun="helm uninstall"

alias hla="helm list -A"

alias hs="helm status"

# kubectl
alias kdb="kubectl get -n db all"
alias kapps="kubectl get -n apps all"

# clean pvc
alias kpvault="kubectl patch pv vault-master-0-pv -p '{\"spec\":{\"claimRef\": null}}'"

alias kpvpg="kubectl patch pv postgres-primary-0-pv -p '{\"spec\":{\"claimRef\": null}}' \
            && kubectl patch pv postgres-replica-0-pv -p '{\"spec\":{\"claimRef\": null}}'"

alias kpvre="kubectl patch pv redis-master-0-pv -p '{\"spec\":{\"claimRef\": null}}' \
            && kubectl patch pv redis-replica-0-pv -p '{\"spec\":{\"claimRef\": null}}'"

alias kpvmo="kubectl patch pv mongodb-master-0-pv -p '{\"spec\":{\"claimRef\": null}}'"
