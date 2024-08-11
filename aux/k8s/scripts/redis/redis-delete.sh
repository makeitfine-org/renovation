#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2024
#

kubectl delete service redis
kubectl delete deployments.apps redis
