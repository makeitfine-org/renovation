#!/usr/bin/env bash
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2025
#

wait_for_pod() {
  local NAMESPACE="$1"
  local PATTERN="$2"
  local STATUS="$3"

  while ! kubectl get pods -n "$NAMESPACE" 2>/dev/null \
    | grep "$PATTERN" \
    | awk '{print $3}' \
    | grep -q "^$STATUS$"; do
      echo "Waiting for any pod matching '$PATTERN' in namespace '$NAMESPACE' to be '$STATUS'..."
      sleep 2
  done

  echo "✅ A pod matching '$PATTERN' in namespace '$NAMESPACE' reached status '$STATUS'!"
}

wait_for_entity() {
  local NAMESPACE="$1"
  local PATTERN="$2"
  local ENTITY="$3"

  until kubectl get "$ENTITY" -n "$NAMESPACE" 2>/dev/null | grep -q "$PATTERN"; do
    echo "Waiting for '$ENTITY' matching '$PATTERN' in namespace '$NAMESPACE' to appear..."
    sleep 2
  done

  echo "✅ '$ENTITY' matching '$PATTERN' now exists in namespace '$NAMESPACE'!"
}

wait_for_pods_to_disappear() {
  local NAMESPACE="$1"
  local PATTERN="$2"

  while kubectl get pods -n "$NAMESPACE" 2>/dev/null | grep -q "$PATTERN"; do
    echo "Waiting for pods matching '$PATTERN' in namespace '$NAMESPACE' to disappear..."
    sleep 2
  done

  echo "✅ No pods matching '$PATTERN' remain in namespace '$NAMESPACE'!"
}

wait_for_secret_to_disappear() {
  local NAMESPACE="$1"
  local PATTERN="$2"
  local ENTITY="$3"

  while kubectl get "$ENTITY" -n "$NAMESPACE" 2>/dev/null | grep -q "$PATTERN"; do
    echo "Waiting for '$ENTITY' matching '$PATTERN' in namespace '$NAMESPACE' to disappear..."
    sleep 2
  done

  echo "✅ No '$ENTITY'(s) matching '$PATTERN' remain in namespace '$NAMESPACE'!"
}

wait_for_pods_running_and_ready() {
  local NAMESPACE="$1"
  local PATTERN="$2"

  while true; do
    # Get STATUS and READY columns for pods matching the pattern
    NOT_READY=$(kubectl get pods -n "$NAMESPACE" 2>/dev/null \
      | grep "$PATTERN" \
      | awk '{print $2, $3}' \
      | grep -vE '^([0-9]+)/\1 Running$' || true)

    if [[ -z "$NOT_READY" ]]; then
      break
    fi

    echo "Waiting for all pods matching '$PATTERN' in namespace '$NAMESPACE' to be Running and Ready..."
    sleep 2
  done

  echo "✅ All pods matching '$PATTERN' in namespace '$NAMESPACE' are Running and Ready!"
}
