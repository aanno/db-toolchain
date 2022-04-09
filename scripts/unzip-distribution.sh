#!/bin/bash -x

BASE=`git rev-parse --show-toplevel`

pushd "$BASE"

  rm -rf db-toolchain-*/
  unzip build/distributions/db-toolchain-*.zip

popd

