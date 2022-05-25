#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2022
#

kubectl delete service pgpool-svc
kubectl delete service pgpool-svc-external

kubectl delete deployment.apps pgpool-deployment
