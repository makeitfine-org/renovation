#!/bin/bash
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2023
#

starting=true

while
  $starting;
do
    starting=false
    sleep 5
    echo "Waiting for docker compose services starting ..."

    function check_url() {
        if ! curl -s $1 > /dev/null ; then
            echo ">>> $1"
            starting=true
        fi;
    }

    check_url "http://localhost:18080/realms/renovation-realm/.well-known/openid-configuration"
    check_url "http://localhost:8280/about"
    check_url "http://localhost:8281/about"
    check_url "http://localhost:8285/about"
    check_url "http://localhost:9190/about"
    check_url "http://localhost:1280/about"
    check_url "http://localhost:8290/about"
done
