#!/bin/bash

URL="http://l.ua/about"
COUNT=0
TOTAL=100
DELAY=0.001

for i in $(seq 1 $TOTAL); do
    RESPONSE=$(curl -s "$URL")

    if echo "$RESPONSE" | grep -q "backend"; then
        COUNT=$((COUNT + 1))
#        echo "i: $i c: $COUNT"
    fi

    sleep $DELAY
done

echo "Responses containing 'backend': $COUNT out of $TOTAL"
