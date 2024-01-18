#!/bin/bash
set -e

# Define the full path to the scripts directory
SCRIPTS_DIR="./scripts"

# Update the set of services and
# build and execute the system tests
"$SCRIPTS_DIR/build.sh"
"$SCRIPTS_DIR/deploy.sh"
sleep 5
"$SCRIPTS_DIR/test.sh"

# Cleanup the build images
docker image prune -f
