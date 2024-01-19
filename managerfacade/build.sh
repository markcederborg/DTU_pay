#!/bin/bash
set -e
mvn clean install package
docker-compose build managerfacade
