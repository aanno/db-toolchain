#!/bin/bash

ROOT=`git rev-parse --show-toplevel`
TMP="$ROOT/lib/tmp"
DIR="$ROOT/lib/ueberjars"
UEBERJAR1="$DIR/jnrchannels.jar"
UEBERJAR2="$DIR/xmlcalabash-extensions.jar"

JNR_JAR1="$TMP/jnr-unixsocket.jar"
JNR_JAR2="$TMP/jnr-enxio.jar"
CALAB_JAR1="$TMP/xmlcalabash1-mathml-to-svg.jar"
CALAB_JAR2="$TMP/xmlcalabash1-xslthl.jar"

mkdir -p "$TMP"
mkdir -p "$DIR"
rm -rf "$UEBERJAR1" "$UEBERJAR2" "$TMP/jnr" "$TMP/com" "$TMP/scm" "$TMP/META-INF" "$TMP/xercesImpl"

pushd "$TMP"

unzip -q "$JNR_JAR1"
unzip -qo "$JNR_JAR2"
zip -9rq "$UEBERJAR1" jnr "META-INF" -x \*.jar

rm -rf "$TMP/jnr" "META-INF"
unzip -q "$CALAB_JAR1"
unzip -qo "$CALAB_JAR2"
rm com/xmlcalabash/extensions/*.class
zip -9rq "$UEBERJAR2" com "META-INF" -x \*.jar

popd
