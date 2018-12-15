#!/bin/bash

ROOT=`git rev-parse --show-toplevel`
DOWNLOAD="$ROOT/examples/db"

download() {
    local url="$1"
    shift
    local cut="$1"
    shift

    wget -nH -x -r --cut-dirs=$cut --no-parent -k $url
}

TRANSITION=( \
  https://docbook.org/docs/howto/howto.xml \
  https://docbook.org/docs/howto/images/emacs.png \
  https://docbook.org/docs/howto/images/oxygen4.png \
  https://docbook.org/docs/howto/images/oxygen5.png \
  https://docbook.org/docs/howto/images/xxe.png \
)

mkdir -p "$DOWNLOAD/transition"

pushd "$DOWNLOAD/transition"

for i in "${TRANSITION[@]}"; do
    download $i 2
done

popd
