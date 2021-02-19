#!/bin/bash -x

BASE=`git rev-parse --show-toplevel`
TMPDIR=`mktemp -d`

pushd "$BASE"

  rm -rf db-toolchain-*/
  unzip build/distributions/db-toolchain-*.zip

  pushd "$TMPDIR"
    unzip $BASE/db-toolchain-*/lib/jffi-*-native.jar
    zip -ur9 $BASE/db-toolchain-*/lib/jffi-*[0-9].jar .
    rm $BASE/db-toolchain-*/lib/jffi-*-native.jar
  popd

popd

rm -rf "$TMPDIR"
