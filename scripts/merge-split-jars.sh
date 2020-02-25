#!/bin/bash

ROOT=`git rev-parse --show-toplevel`
TMP="$ROOT/lib/tmp"
DIR="$ROOT/lib/ueberjars"
UEBERJAR="$DIR/jnrchannels.jar"

# JAR1="$HOME/.gradle/caches/modules-2/files-2.1/com.github.jnr/jnr-unixsocket/0.20/b683f10dded734c662c418160e314e6db936fdee/jnr-unixsocket-0.20.jar"
# JAR2="$HOME/.gradle/caches/modules-2/files-2.1/com.github.jnr/jnr-enxio/0.19/c7664aa74f424748b513619d71141a249fb74e3e/jnr-enxio-0.19.jar"

JAR1="$TMP/jnr-unixsocket.jar"
JAR2="$TMP/jnr-enxio.jar"

# TMP=`mktemp -d`

mkdir -p "$TMP"
mkdir -p "$DIR"
rm -rf "$UEBERJAR" "$TMP/jnr" "$TMP/META-INF" "$TMP/xercesImpl"

pushd "$DIR"

rm "$UEBERJAR"
# cp "$JAR2" "$UEBERJAR"

popd

pushd "$TMP"

unzip -q "$JAR1"
unzip -qo "$JAR2"
zip -9rq "$UEBERJAR" . -x \*.jar

popd

# rm -r "$TMP"
