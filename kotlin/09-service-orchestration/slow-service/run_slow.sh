#!/usr/bin/env bash

export SLOW_SERVICE_DELAY_IN_SECONDS=10
cd $(dirname $0) && ./gradlew bootRun
