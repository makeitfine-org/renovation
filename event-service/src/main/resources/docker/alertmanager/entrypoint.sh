#!/bin/sh

#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2025
#

set -e

# Replace placeholders with environment variable values in the config file
sed -i "s|\${TELEGRAM_BOT_TOKEN}|${TELEGRAM_BOT_TOKEN}|g" /etc/alertmanager/alertmanager.yml
sed -i "s|\${TELEGRAM_CHAT_ID}|${TELEGRAM_CHAT_ID}|g" /etc/alertmanager/alertmanager.yml

# Start Alertmanager
exec alertmanager --config.file=/etc/alertmanager/alertmanager.yml
