#!/bin/sh
DIR=$(dirname "$0")
export MAVEN_HOME="$DIR/.maven"
"$MAVEN_HOME/bin/mvn" "$@"
