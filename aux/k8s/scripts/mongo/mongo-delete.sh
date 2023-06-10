#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2023
#

kubectl delete service mongo
kubectl delete deployments.apps mongo
kubectl delete pvc mongo-pv-claim
kubectl delete pv mongo-pv-volume