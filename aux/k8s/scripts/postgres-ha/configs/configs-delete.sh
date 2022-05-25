#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2022
#

kubectl delete secrets pgpool-secrets
kubectl delete secrets postgres-ha-secrets
kubectl delete configmaps postgres-ha-configmap