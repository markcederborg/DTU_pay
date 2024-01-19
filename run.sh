#!/bin/bash
set -e
docker image prune -f
docker-compose up -d rabbitmq
sleep 10
docker-compose up accountservice customerfacade idgeneratorservice managerfacade merchantfacade paymentservice reportservice tokenservice

