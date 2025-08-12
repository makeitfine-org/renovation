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

#postgres:
export pg_username='postgres';
export pg_password='postgres1';
export pg_url='jdbc:postgresql://192.168.49.2:30432/renovation';
export pg_db='renovation';
export pg_schema='backend';

#
#redis:
#password: redispass
#url: jdbc:redis://192.168.49.2:30379/
#
#mongo:
#user: infouser
#pass: infopassword
#url: mongodb://192.168.49.2:30017/infodb

# test postgres
if nc -zv "$CLUSTER_IP" "$POSTGRES_CLUSTER_PORT" 2>&1 | grep -q 'succeeded'; then
  echo "✅ Connection to PostgreSQL ($CLUSTER_IP:$POSTGRES_CLUSTER_PORT) succeeded"

  result=$(PGPASSWORD="$pg_password" psql -h "$CLUSTER_IP" -p "$POSTGRES_CLUSTER_PORT" -U "$pg_username" -d "$pg_db" -c "SET search_path TO $pg_schema; SELECT * FROM work LIMIT 1;")
    if [ $? -eq 0 ] && [ -n "$result" ]; then
      echo "✅✅ Query succeeded"
    else
      echo "❌❌ Query failed or returned no data"
      exit;
    fi
else
  echo "❌ Connection to PostgreSQL ($CLUSTER_IP:$POSTGRES_CLUSTER_PORT) failed"
  exit;
fi

# test redis
if nc -zv "$CLUSTER_IP" "$REDIS_CLUSTER_PORT" 2>&1 | grep -q 'succeeded'; then
  echo "✅ Connection to Redis ($CLUSTER_IP:$REDIS_CLUSTER_PORT) succeeded"

  #todo: query to redis
else
  echo "❌ Connection to Redis ($CLUSTER_IP:$REDIS_CLUSTER_PORT) failed"
  exit;
fi

# test mongo
if nc -zv "$CLUSTER_IP" "$MONGO_CLUSTER_PORT" 2>&1 | grep -q 'succeeded'; then
  echo "✅ Connection to Mongo ($CLUSTER_IP:$MONGO_CLUSTER_PORT) succeeded"

  #todo: query to mongo
else
  echo "❌ Connection to Mongo ($CLUSTER_IP:$MONGO_CLUSTER_PORT) failed"
  exit;
fi

# test apps:
newman run renovation-minikube.postman_collection.json -e renovation-minikube-env.postman_environment.json
