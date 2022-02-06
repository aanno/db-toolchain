#!/bin/bash -x

BASE=`git rev-parse --show-toplevel`
MYDIR=`ls -d $BASE/db-toolchain-*`
MYDIR=`readlink -f $MYDIR`

. "$BASE/scripts/env.sh"
$JAVA_HOME/bin/java $JAVA_OPTS -p $MYDIR:$MYDIR/lib -m com.github.aanno.dbtoolchain/com.github.aanno.dbtoolchain.App $*
