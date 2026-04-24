#!/usr/bin/env bash
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2026
#

# PostToolUse hook: after editing a Kotlin file, remind to run ktlintFormat.
# Advisory only — exits 0.

set -eu

if ! command -v jq >/dev/null 2>&1; then
  exit 0
fi

payload=$(cat || true)
[ -z "${payload}" ] && exit 0

file_path=$(printf '%s' "${payload}" | jq -r '.tool_input.file_path // empty' 2>/dev/null || true)
[ -z "${file_path}" ] && exit 0

case "${file_path}" in
  *.kt|*.kts)
    printf '\033[36m[renovation-hook]\033[0m Kotlin file edited — run \033[1m./gradlew ktlintFormat\033[0m before committing.\n' >&2
    ;;
esac

exit 0
