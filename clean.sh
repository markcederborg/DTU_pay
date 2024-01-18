#!/bin/bash
set -e

# Build and install the libraries
# abstracting away from using the
# RabbitMq message queue
pushd messaging-utilities-3.4
mvn clean
popd

pushd dtupay
mvn clean
popd


# Build the services
pushd accountservice
mvn clean
popd

pushd customerfacade
mvn clean
popd

pushd idgeneratorservice
mvn clean
popd

pushd managerfacade
mvn clean
popd

pushd merchantfacade
mvn clean
popd

pushd paymentservice
mvn clean
popd

pushd reportservice
mvn clean
popd

pushd tokenservice
mvn clean
popd
