#!/bin/bash -x

BASE=`git rev-parse --show-toplevel`
MYDIR=`ls -d $BASE/db-toolchain-*`
MYDIR=`readlink -f $MYDIR`

# FilenoUtil : Native subprocess control requires open access to the JDK IO subsystem
JRUBY_OPTS="--add-opens java.base/sun.nio.ch=org.jruby.core --add-opens java.base/java.io=org.jruby.core"

. "$BASE/scripts/env.sh"
$JAVA_HOME/bin/java $JRUBY_OPTS $JAVA_OPTS -p $MYDIR:$MYDIR/lib -m com.github.aanno.dbtoolchain/com.github.aanno.dbtoolchain.App $*
