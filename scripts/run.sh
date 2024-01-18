#!/bin/bash
set -e
docker image prune -f
docker-compose up -d rabbitmq
sleep 4
docker-compose up customerfacade merchantfacade managerfacade accountservice paymentservice tokenservice idgeneratorservice reportservice

