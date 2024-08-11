#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2024
#

kubectl delete service postgres
kubectl delete deployments.apps postgres
kubectl delete pvc postgres-pv-claim
kubectl delete pv postgres-pv-volume
