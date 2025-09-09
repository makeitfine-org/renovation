#!/bin/sh
#set -x // verbose commands

CURRENT_PATH="$(realpath "$(dirname "$0")")"

kubectl -n db delete pod postgresql-primary-0

sleep 30

sh "$CURRENT_PATH"/../test/test.sh
