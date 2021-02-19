#!/bin/bash -x

BASE=`git rev-parse --show-toplevel`

pushd $BASE/db-toolchain-*/

  # TODO aanno: problematic - but why?
  # java -p ".:lib" -jar imap2signal-gateway-*.jar

  # Working with explizit module name
  java -p .:lib -m com.github.aanno.dbtoolchain/com.github.aanno.dbtoolchain.App $*

popd
