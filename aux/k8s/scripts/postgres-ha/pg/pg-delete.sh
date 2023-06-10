#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2023
#

kubectl delete service postgres-ha-headless-svc
kubectl delete statefulset.apps postgres-ha-sts

kubectl delete pvc postgres-ha-data-postgres-ha-sts-0
kubectl delete pvc postgres-ha-data-postgres-ha-sts-1
kubectl delete pvc postgres-ha-data-postgres-ha-sts-2

kubectl delete pv postgres-ha-data-0
kubectl delete pv postgres-ha-data-1
kubectl delete pv postgres-ha-data-2