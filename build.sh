#!/bin/bash
set -e

# Build and install the libraries
# abstracting away from using the
# RabbitMq message queue
pushd messaging-utilities-3.4
./build.sh
popd

pushd dtupay
./build.sh
popd


# Build the services
pushd accountservice
./build.sh
popd

pushd customerfacade
./build.sh
popd

pushd idgeneratorservice
./build.sh
popd

pushd managerfacade
./build.sh
popd

pushd merchantfacade
./build.sh
popd

pushd paymentservice
./build.sh
popd

pushd reportservice
./build.sh
popd

pushd tokenservice
./build.sh
popd
