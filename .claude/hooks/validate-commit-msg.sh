#!/usr/bin/env bash
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2026
#

# PreToolUse hook: validate commit message format against the project's
# git-hook regex: "#<1-4 digits> <desc>" or "WIP <desc>", description <= 80 chars.
# Advisory only — prints a warning on mismatch, never blocks.

set -eu

if ! command -v jq >/dev/null 2>&1; then
  exit 0
fi

payload=$(cat || true)
[ -z "${payload}" ] && exit 0

cmd=$(printf '%s' "${payload}" | jq -r '.tool_input.command // empty' 2>/dev/null || true)
[ -z "${cmd}" ] && exit 0

case "${cmd}" in
  *"git commit"*-m*|*"git commit"*--message*) ;;
  *) exit 0 ;;
esac

msg=$(printf '%s' "${cmd}" | sed -n "s/.*-m[[:space:]]*['\"]\\([^'\"]*\\)['\"].*/\\1/p" | head -n1)
[ -z "${msg}" ] && exit 0

if printf '%s' "${msg}" | grep -Eq '^(#[0-9]{1,4} .{1,80}|WIP .{1,80})$'; then
  exit 0
fi

printf '\033[33m[renovation-hook]\033[0m commit message may not match repo convention:\n' >&2
printf '  got:      %q\n' "${msg}" >&2
printf '  expected: "#<task-number> <description up to 80 chars>"  OR  "WIP <description>"\n' >&2
printf '  (the pre-push git hook will reject non-matching messages)\n' >&2
exit 0
