#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2023
#

kubectl delete secrets pgpool-secrets
kubectl delete secrets postgres-ha-secrets
kubectl delete configmaps postgres-ha-configmap