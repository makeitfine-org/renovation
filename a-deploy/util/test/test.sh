#!/bin/sh
#set -x // verbose commands

#MINIKUBE_PATH="`dirname \"$0\"`"
#K8S_PATH="${MINIKUBE_PATH}/../../k8s"
#echo "minikube path: $MINIKUBE_PATH ($(pwd))"

export CLUSTER_NAME='newc';
export CLUSTER_IP='192.168.49.2';

export POSTGRES_CLUSTER_PORT=30432;

export REDIS_CLUSTER_PORT=30379;

export MONGO_CLUSTER_PORT=30017;

# postgres:
export pg_username='postgres';
export pg_password='postgres1';

# shellcheck disable=SC2016
export pg_url='jdbc:postgresql://$CLUSTER_IP:$POSTGRES_CLUSTER_PORT/renovation';
export pg_db='renovation';
export pg_schema='backend';

#redis:
redis_password='redispass';

#mongo:
mongo_db='infodb'
mongo_user='infouser';
mongo_pass='infopassword'


# test cluster reachable
if ping -c 1 "$CLUSTER_IP" >/dev/null 2>&1; then
    echo "✅ Cluster Host ($CLUSTER_NAME -> $CLUSTER_IP) is reachable"
else
    echo "❌ Cluster Host ($CLUSTER_NAME -> $CLUSTER_IP) is not reachable"
    exit 1;
fi

# test postgres
if nc -zv "$CLUSTER_IP" "$POSTGRES_CLUSTER_PORT" 2>&1 | grep -q 'succeeded'; then
  echo "✅ Connection to PostgreSQL ($CLUSTER_IP:$POSTGRES_CLUSTER_PORT) succeeded"

  # install `psql` if no
  result=$(PGPASSWORD="$pg_password" psql -h "$CLUSTER_IP" -p "$POSTGRES_CLUSTER_PORT" -U "$pg_username" -d "$pg_db" -c "SET search_path TO $pg_schema; SELECT * FROM work LIMIT 1;")
    if [ $? -eq 0 ] && [ -n "$result" ]; then
      echo "✅✅ Query succeeded"
    else
      echo "❌❌ Query failed or returned no data"
      exit 1;
    fi
else
  echo "❌ Connection to PostgreSQL ($CLUSTER_IP:$POSTGRES_CLUSTER_PORT) failed"
  exit 1;
fi

# test redis
if nc -zv "$CLUSTER_IP" "$REDIS_CLUSTER_PORT" 2>&1 | grep -q 'succeeded'; then
  echo "✅ Connection to Redis ($CLUSTER_IP:$REDIS_CLUSTER_PORT) succeeded"

  # install redis-cli
  if [ "$(redis-cli -h "$CLUSTER_IP" -p "$REDIS_CLUSTER_PORT" -a "$redis_password" PING 2>/dev/null)" = "PONG" ]; then
    echo "✅✅ Redis AUTH successful"

    result=$(redis-cli -h "$CLUSTER_IP" -p "$REDIS_CLUSTER_PORT" -a "$redis_password" keys '*' 2>/dev/null)
    if [ -n "$result" ]; then
      echo "✅✅ Keys found"
    else
      echo "✅✅ No keys found in Redis"
    fi
  else
    echo "❌ Redis AUTH failed or no connection"
    exit 1;
  fi
else
  echo "❌ Connection to Redis ($CLUSTER_IP:$REDIS_CLUSTER_PORT) failed"
  exit 1;
fi

# test mongo
if nc -zv "$CLUSTER_IP" "$MONGO_CLUSTER_PORT" 2>&1 | grep -q 'succeeded'; then
  echo "✅ Connection to Mongo ($CLUSTER_IP:$MONGO_CLUSTER_PORT) succeeded"

  # install mongosh (https://www.mongodb.com/docs/mongodb-shell/install/)
  count=$(mongosh --quiet \
      --host "$CLUSTER_IP" \
      --port "$MONGO_CLUSTER_PORT" \
      -u "$mongo_user" \
      -p "$mongo_pass" \
      --authenticationDatabase "$mongo_db" \
      --eval "db.getSiblingDB('$mongo_db').details.countDocuments({})")

    if [ "$count" -ge 1 ] 2>/dev/null; then
      echo "✅✅ MongoDB 'details' collection has at least one record (count=$count)"
    else
      echo "❌❌ MongoDB 'details' collection is empty or query failed (count=$count)"
      exit 1
    fi
else
  echo "❌ Connection to Mongo ($CLUSTER_IP:$MONGO_CLUSTER_PORT) failed"
  exit 1;
fi

# test apps: (npm install -g newman)
newman run renovation-minikube.postman_collection.json -e renovation-minikube-env.postman_environment.json
