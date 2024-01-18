#!/bin/bash
set -e

pushd messaging-utilities-3.3
mvn clean
popd

pushd managerfacade
mvn clean
popd

pushd customerfacade
mvn clean
popd

pushd merchantfacade
mvn clean
popd

pushd accountservice
mvn clean
popd

pushd paymentservice
mvn clean
popd

pushd tokenservice
mvn clean
popd

pushd idgeneratorservice
mvn clean
popd

pushd reportservice
mvn clean
popd




