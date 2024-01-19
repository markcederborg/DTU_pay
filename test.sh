#!/bin/bash
set -e
pushd end-to-end-tests
mvn clean test
popd