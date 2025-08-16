#!/bin/sh
#set -x // verbose commands

kubectl -n db delete pod postgresql-primary-0

sleep 30

sh test.sh
