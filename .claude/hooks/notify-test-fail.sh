#!/usr/bin/env bash
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2026
#

# PostToolUse hook: after a Gradle test-ish command, surface any FAILED lines.
# Advisory only — exits 0.

set -eu

if ! command -v jq >/dev/null 2>&1; then
  exit 0
fi

payload=$(cat || true)
[ -z "${payload}" ] && exit 0

cmd=$(printf '%s' "${payload}" | jq -r '.tool_input.command // empty' 2>/dev/null || true)
[ -z "${cmd}" ] && exit 0

case "${cmd}" in
  *"./gradlew"*test*|*"./gradlew"*Test*|*"./gradlew buildAll"*|*"./gradlew ba"*) ;;
  *) exit 0 ;;
esac

out=$(printf '%s' "${payload}" | jq -r '(.tool_response.stdout // "") + "\n" + (.tool_response.stderr // "")' 2>/dev/null || true)
[ -z "${out}" ] && exit 0

if printf '%s' "${out}" | grep -qE 'FAILED|BUILD FAILED'; then
  printf '\033[31m[renovation-hook]\033[0m Gradle reported failures. Failing lines:\n' >&2
  printf '%s' "${out}" | grep -E 'FAILED|BUILD FAILED' | head -20 >&2
  printf '  Hint: re-run with \033[1m-i\033[0m for info logs, or \033[1m--tests "ClassName.method*"\033[0m to isolate.\n' >&2
fi

exit 0
