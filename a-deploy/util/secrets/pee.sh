#!/bin/sh

# parse_export_envvars.sh

if [ $# -lt 1 ]; then
  echo "Usage: $0 <path-to-json-file>"
  exit 1
fi

JSON_FILE="$1"

if [ ! -f "$JSON_FILE" ]; then
  echo "File not found: $JSON_FILE"
  exit 1
fi

# Parse JSON and export each key=value
for kv in $(jq -r 'to_entries | .[] | "\(.key)=\(.value)"' "$JSON_FILE"); do
  export "$kv"
done

# Optional: print them to verify
echo "Exported environment variables:"
jq -r 'keys[]' "$JSON_FILE" | while read -r key; do
  eval echo "$key=\$$key"
done
