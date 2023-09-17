#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2023
#
echo "=== Add secrets ==="
#vault kv put secret/renovation_temp value.value1="Alles" value.value2="Hoppvault kv list -mount secret!"
#vault kv put secret/renovation_temp/vault value.value1="Alles new" value.value2="Hoppvault kv list -mount secret! new"

#sleep 3
#curl -X 'GET' 'http://127.0.0.1:8400/v1/secret/data/foo' -H 'accept: application/json' -H 'X-Vault-Token: 00000000-0000-0000-0000-000000000000'
curl -X 'POST' \
  'http://renovation-vault:8200/v1/secret/data/renovation_temp' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H 'X-Vault-Token: 00000000-0000-0000-0000-000000000000' \
  -d '{
  "data": {"value.value1":"Hello!", "value.value2":"There!!!"}
}'
#sleep 1
curl -X 'POST' \
  'http://renovation-vault:8200/v1/secret/data/renovation_temp%2Fvault' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H 'X-Vault-Token: 00000000-0000-0000-0000-000000000000' \
  -d '{
  "data": {"value.value1":"Next Hello!", "value.value2":"Next there!!!"}
}'
