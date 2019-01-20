#!/bin/bash

ROOT=`git rev-parse --show-toplevel`

pushd "$ROOT"

    ./scripts/download-needed.sh

    git submodule init
    git submodule update

    pushd submodules

        pushd xmlcalabash1-print
            git remote add upstream git@github.com:ndw/xmlcalabash1-print.git
        popd

        pushd asciidoctorj
            git remote add upstream git@github.com:asciidoctor/asciidoctorj.git
        popd

        pushd xslt20-stylesheets
            touch settings.gradle.kts
        popd

        pushd xslt20-resoures
            touch settings.gradle.kts
        popd

        pushd jing-trang
            git remote add upstream git@github.com:relaxng/jing-trang.git
            touch settings.gradle.kts
            ../../gradlew build
        popd

        pushd fop
            git remote add upstream git@github.com:apache/fop.git
            mvn -DskipTests clean package
        popd

        pushd batik
            git remote add upstream git@github.com:apache/batik.git
            mvn -DskipTests clean package
        popd

    popd

    git submodule foreach git gc

    ./gradlew copyJarsForUeberJars
    ./scripts/merge-split-jars.sh

    ./gradlew build

popd
