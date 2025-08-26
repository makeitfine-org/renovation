#!/bin/sh
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2025
#

# check_namespaces_exit.sh

# Namespaces to check
NAMESPACES="infra db apps security"

while true; do
  # Get current namespaces
  CURRENT_NS=$(kubectl get namespaces -o jsonpath='{.items[*].metadata.name}')

  # Check if all required namespaces are missing
  all_missing=true
  for ns in $NAMESPACES; do
    echo "$CURRENT_NS" | grep -qw "$ns" && all_missing=false
  done

  if [ "$all_missing" = true ]; then
    echo "removed namespaces"
    break
  fi

  sleep 1
done
