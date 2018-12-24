#!/bin/bash

ROOT=`git rev-parse --show-toplevel`
DIR="$ROOT/lib/ueberjars"
ADJUBER="$DIR/asciidocj.jar"

ADJ="/home/tpasch/.gradle/caches/modules-2/files-2.1/org.asciidoctor/asciidoctorj/1.6.0-RC.2/58a312abba8fe47d3a4e7e1b10f40cda6fcad740/asciidoctorj-1.6.0-RC.2.jar"
ADJAPI="/home/tpasch/.gradle/caches/modules-2/files-2.1/org.asciidoctor/asciidoctorj-api/1.6.0-RC.2/cddb879b9ff205212eddcda62992816710553315/asciidoctorj-api-1.6.0-RC.2-sources.jar"

TMP=`mktemp -d`

mkdir -p "$DIR"
pushd "$DIR"

rm "$ADJUBER"
cp "$ADJAPI" "$ADJUBER"

popd

pushd "$TMP"

unzip -q "$ADJ"
zip -u9rq "$ADJUBER" .

popd

rm -r "$TMP"
