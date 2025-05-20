#!/bin/bash
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2025
#

#!/bin/bash

#!/bin/bash

# turn on bash's job control
set -e

echo "⏳ Waiting for Neo4j to start..."

# Start Neo4j in the background
/startup/docker-entrypoint.sh neo4j &

# Wait for Bolt port to be available
until cypher-shell -u "$noe4juser" -p "$noe4jpass" "RETURN 1" >/dev/null 2>&1; do
  echo "⏳ Waiting for database connection..."
  sleep 2
done

echo "✅ Connected to Neo4j. Running migrations..."

for f in /init/*.cypher; do
  echo "⚙️ Executing $f"
  cypher-shell -u "$noe4juser" -p "$noe4jpass" < "$f"
done

echo "✅ All migrations executed."

# Wait for background Neo4j
wait
