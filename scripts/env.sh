#!/bin/bash

JAVA_DEBUG="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005"

# no debug
JAVA_OPTS=""
# for debug
JAVA_OPTS="$JAVA_DEBUG"
