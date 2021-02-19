#!/bin/bash -x

BASE=`git rev-parse --show-toplevel`
MYDIR=`ls -d $BASE/db-toolchain-*`
MYDIR=`readlink -f $MYDIR`

java -p $MYDIR:$MYDIR/lib -m com.github.aanno.dbtoolchain/com.github.aanno.dbtoolchain.App $*
