#!/bin/bash -x

ROOT=`git rev-parse --show-toplevel`

pushd "$ROOT"

./scripts/run-with-modulepath.sh transform -d . -w ./submodules/asciidoctor.org/docs \
  --pipeline xsl20-fo -of HTML5 \
  -i ./submodules/asciidoctor.org/docs/asciidoc-writers-guide.adoc
echo -e "\n\n"

./scripts/run-with-modulepath.sh transform -d . -w ./submodules/asciidoctor.org/docs \
  --pipeline xsl20-fo -of PDF \
  -i ./submodules/asciidoctor.org/docs/asciidoc-writers-guide.adoc
echo -e "\n\n"

./scripts/run-with-modulepath.sh transform -d . -w ./submodules/asciidoctor.org/docs \
  --pipeline xsl20-css -of HTML5 \
  -i ./submodules/asciidoctor.org/docs/asciidoc-writers-guide.adoc
echo -e "\n\n"

./scripts/run-with-modulepath.sh transform -d . -w ./submodules/asciidoctor.org/docs \
  --pipeline xsl20-css -of PDF --princeapi \
  -i ./submodules/asciidoctor.org/docs/asciidoc-writers-guide.adoc
echo -e "\n\n"

popd
