#!/bin/bash
set -e

# Build and install the libraries
# abstracting away from using the
# RabbitMq message queue
cd ./messaging-utilities-3.4
./build.sh
cd -

cd ./dtupay
./build.sh
cd -


# Build the services
cd ./accountservice
./build.sh
cd -

cd ./customerfacade
./build.sh
cd -

cd ./idgeneratorservice
./build.sh
cd -

cd ./managerfacade
./build.sh
cd -

cd  ./merchantfacade
./build.sh
cd -

cd ./paymentservice
./build.sh
cd -

cd ./reportservice
./build.sh
cd -

cd ./tokenservice
./build.sh
cd -
