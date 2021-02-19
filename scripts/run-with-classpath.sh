#!/bin/bash -x

BASE=`git rev-parse --show-toplevel`
CP=""

pushd $BASE/db-toolchain-*

  APP=`ls db-toolchain-*.jar`
  for i in lib/*.jar; do
    CP="$CP:$i"
  done

  java -cp "${APP}${CP}" com.github.aanno.dbtoolchain.App $*

popd
