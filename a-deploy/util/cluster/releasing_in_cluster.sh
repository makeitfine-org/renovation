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

# shellcheck disable=SC2317
echo "minikube path: $MINIKUBE_PATH ($(pwd))"

minikube profile "$CLUSTER_NAME"

# 0 Such env. vars should be added:
#
# `export RENOVATION_VAULT_TOKEN`
# `export RENOVATION_VAULT_UNSEAL_KEY`
# todo: un-commend 3 lines
#helm repo add bitnami   https://charts.bitnami.com/bitnami
#helm repo add hashicorp https://helm.releases.hashicorp.com
#helm repo add external  https://charts.external-secrets.io
#helm repo update

# Install chart dependencies
# todo: un-commend
#sh "$CURRENT_PATH"/../helper/update_dependencies.sh

# create namespaces:
kubectl create namespace security
kubectl create namespace db
kubectl create namespace apps

wait_for_pods() {
  local NAMESPACE="$1"
  local PATTERN="$2"
  local STATUS="$3"

  while kubectl get pods -n "$NAMESPACE" | grep "$PATTERN" | grep -vq "$STATUS"; do
    echo "Waiting for pods matching '$PATTERN' in namespace '$NAMESPACE'..."
    sleep 2
  done

  echo "✅ All pods matching '$PATTERN' in namespace '$NAMESPACE' are running!"
}

# 1 (external-secrets)
#
helm install extrs . --set externalc.enabled=true -n security --create-namespace
helm -n security status extrs
wait_for_pods security extrs-external-secrets Running

# 2 (vault)
#
helm install vaultrs . --set vaultc.enabled=true --set vaultc.vault.token=$RENOVATION_VAULT_TOKEN --set vaultc.vault.unsealKey=$RENOVATION_VAULT_UNSEAL_KEY -n security --create-namespace
helm -n security status vaultrs
wait_for_pods security pod/vaultrs-vaultc-seed Running
wait_for_pods security pod/vaultrs-vaultc-seed Completed

#
## 3 (secrets from vault)
##
#helm install secrets . --set global.secrets.enabled=true  -n security --create-namespace
#helm -n security status secretsrs
#sleep 15
#
#helm plugin install https://github.com/jkroepke/helm-secrets
#
#export VAULT_ADDR="http://192.168.49.2:30820"
#export VAULT_TOKEN=$RENOVATION_VAULT_TOKEN
#
## secrets
## vault kv get secret/renovation/secrets
#
## 4 (postgres)
##
## Install vault
##curl -fsSL https://apt.releases.hashicorp.com/gpg | sudo gpg --dearmor -o /usr/share/keyrings/hashicorp-archive-keyring.gpg
##echo "deb [signed-by=/usr/share/keyrings/hashicorp-archive-keyring.gpg] https://apt.releases.hashicorp.com $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/hashicorp.list
##sudo apt update
##sudo apt install vault
##vault --version
#
#helm install postgresrs . \
#  --set postgresqlc.enabled=true \
#  --set postgresqlc.postgresql.auth.username="$(vault kv get -field=POSTGRES_USER secret/renovation/secrets)" \
#  --set postgresqlc.postgresql.auth.password="$(vault kv get -field=POSTGRES_PASSWORD secret/renovation/secrets)" \
#  --set postgresqlc.postgresql.auth.database="$(vault kv get -field=POSTGRES_DB secret/renovation/secrets)" \
#  --set postgresqlc.postgresql.auth.replicationUsername="$(vault kv get -field=POSTGRES_USER secret/renovation/secrets)" \
#  --set postgresqlc.postgresql.auth.replicationPassword="$(vault kv get -field=POSTGRES_PASSWORD secret/renovation/secrets)" \
#  --set postgresqlc.postgresql.schema="$(vault kv get -field=POSTGRES_SCHEMA secret/renovation/secrets)" \
#  -n db --create-namespace
#
#
#
#echo "helm list -A"
#helm list -A
#
##some check in a while in browser:
## http://192.168.49.2:30080/fe/work
## http://192.168.49.2:30080/fe/worker
##http://192.168.49.2:30090/graphiql?query=%23%20Welcome%20to%20GraphiQL%0A%23%0A%23%20GraphiQL%20is%20an%20in-browser%20tool%20for%20writing%2C%20validating%2C%20and%0A%23%20testing%20GraphQL%20queries.%0A%23%0A%23%20Type%20queries%20into%20this%20side%20of%20the%20screen%2C%20and%20you%20will%20see%20intelligent%0A%23%20typeaheads%20aware%20of%20the%20current%20GraphQL%20type%20schema%20and%20live%20syntax%20and%0A%23%20validation%20errors%20highlighted%20within%20the%20text.%0A%23%0A%23%20GraphQL%20queries%20typically%20start%20with%20a%20%22%7B%22%20character.%20Lines%20that%20start%0A%23%20with%20a%20%23%20are%20ignored.%0A%23%0A%23%20An%20example%20GraphQL%20query%20might%20look%20like%3A%0A%23%0A%23%20%20%20%20%20%7B%0A%23%20%20%20%20%20%20%20field(arg%3A%20%22value%22)%20%7B%0A%23%20%20%20%20%20%20%20%20%20subField%0A%23%20%20%20%20%20%20%20%7D%0A%23%20%20%20%20%20%7D%0A%23%0A%23%20Keyboard%20shortcuts%3A%0A%23%0A%23%20%20%20Prettify%20query%3A%20%20Shift-Ctrl-P%20(or%20press%20the%20prettify%20button)%0A%23%0A%23%20%20Merge%20fragments%3A%20%20Shift-Ctrl-M%20(or%20press%20the%20merge%20button)%0A%23%0A%23%20%20%20%20%20%20%20%20Run%20Query%3A%20%20Ctrl-Enter%20(or%20press%20the%20play%20button)%0A%23%0A%23%20%20%20%20Auto%20Complete%3A%20%20Ctrl-Space%20(or%20just%20start%20typing)%0A%23%0A%0A%20query%7B%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20details%7B%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20id%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20name%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20surname%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20age%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20%7D%0A%20%20%20%20%20%20%20%20%20%20%20%20%7D
#
##check redis (in log should be address only once to db)
##http http://192.168.49.2:30080/api/work
##http http://192.168.49.2:30080/api/work/55555555-a845-45d7-aea9-ab624172d1c1
#
