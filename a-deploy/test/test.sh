#!/bin/sh
set -x // verbose commands

# test postgres

# test redis

# test mongo

# test apps:
newman run renovation-minikube.postman_collection.json -e renovation-minikube-env.postman_environment.json
