#!/bin/bash

ROOT=`git rev-parse --show-toplevel`
DOWNLOAD_TARGET="$ROOT/examples/db"
# TMP=`mktemp -d`
DOWNLOAD_TMP="$ROOT/downloads"

download() {
    local url="$1"
    shift
    local cut="$1"
    shift

    local filename=`basename "$url"`

    pushd "$DOWNLOAD_TMP"
        if [ ! -f "$filename" ]; then
            wget -nH -x -r --cut-dirs=$cut --no-parent -k $url
        fi
    popd
}

TRANSITION=( \
  https://docbook.org/docs/howto/howto.xml \
  https://docbook.org/docs/howto/images/emacs.png \
  https://docbook.org/docs/howto/images/oxygen4.png \
  https://docbook.org/docs/howto/images/oxygen5.png \
  https://docbook.org/docs/howto/images/xxe.png \
)

mkdir -p "$DOWNLOAD_TARGET/transition" "$DOWNLOAD_TMP"
pushd "$DOWNLOAD_TARGET/transition"

for i in "${TRANSITION[@]}"; do
    download $i 2
done

popd

download https://docbook.org/xml/5.1/docbook-v5.1-os.zip 2
download https://docbook.org/xml/5.0/docbook-5.0.zip 2
download https://www.princexml.com/download/prince-java-20180929.zip 1
download https://github.com/docbook/xslt20-stylesheets/releases/download/2.4.3/docbook-xslt2-2.4.3.jar 5
download  https://github.com/docbook/xslt10-stylesheets/releases/download/release/1.79.2/docbook-xsl-1.79.2.zip 6

mkdir -p "schema/5.1"
pushd "schema/5.1"
unzip -o "$DOWNLOAD_TMP/docbook-v5.1-os.zip"
popd

mkdir -p "schema/5.0"
pushd "schema/5.0"
unzip -o "$DOWNLOAD_TMP/docbook-5.0.zip"
popd

mkdir -p "lib"
pushd "lib"

unzip -o "$DOWNLOAD_TMP/prince-java-20180929.zip"
ln -s "prince-java-20180929" "prince-java"

cp "$DOWNLOAD_TMP/docbook-xslt2-2.4.3.jar" .

unzip -o "$DOWNLOAD_TMP/docbook-xsl-1.79.2.zip"
ln -s "docbook-xsl-1.79.2" "docbook-xsl"

popd

# rm -r $TMP
