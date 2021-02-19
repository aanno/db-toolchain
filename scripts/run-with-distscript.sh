#!/bin/bash -x

BASE=`git rev-parse --show-toplevel`

pushd $BASE/db-toolchain-*/

  bin/db-toolchain $*

popd
